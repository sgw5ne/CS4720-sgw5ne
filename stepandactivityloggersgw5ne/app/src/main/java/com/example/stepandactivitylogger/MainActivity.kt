package com.example.stepandactivitylogger

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stepandactivitylogger.data.AppDb
import com.example.stepandactivitylogger.data.StepRepository
import com.example.stepandactivitylogger.sensors.StepSensorManager
import com.example.stepandactivitylogger.ui.theme.StepandactivityloggerTheme
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {

    private lateinit var repository : StepRepository
    private lateinit var vm: StepViewModel
    private lateinit var sensors: StepSensorManager

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            val permanentlyDenied = !granted &&
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                    !shouldShowRequestPermissionRationale(Manifest.permission.ACTIVITY_RECOGNITION)

            vm.onPermissionResult(granted, permanentlyDenied)
            startSensors()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = StepRepository(AppDb.get(applicationContext).dayStepsDao())
        vm = ViewModelProvider(this, StepViewModel.factory(repository))[StepViewModel::class.java]

        val db = AppDb.get(applicationContext)
        val dao = db.dayStepsDao()

        sensors = StepSensorManager(
            context = applicationContext,
            onAccelerometer = { x, y, z, smoothed -> vm.onAccelerometer(x, y, z, smoothed) },
            onStepDetected = { vm.onStepDetected() },
            onCounterReading = { total -> vm.onCounterReading(total) }
        )
        setContent {
            val state by vm.uiState.collectAsStateWithLifecycle()
            val nav = rememberNavController()
            val backStackEntry by nav.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            selected = currentRoute == Routes.LIVE,
                            onClick = { nav.popBackStack(Routes.LIVE, inclusive = false) },
                            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                            label = { Text("Live") }
                        )
                        NavigationBarItem(
                            selected = currentRoute == Routes.HISTORY,
                            onClick = { nav.navigate(Routes.HISTORY) { launchSingleTop = true } },
                            icon = { Icon(Icons.Filled.DateRange, contentDescription = null) },
                            label = { Text("History") }
                        )
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = nav,
                    startDestination = Routes.LIVE,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(Routes.LIVE) {
                        LiveScreen(
                            state = state,
                            onRequestPermission = { requestActivityRecognition() },
                            onOpenSettings = { openAppSettings() }
                        )
                    }
                    composable(Routes.HISTORY) {
                        val history by vm.history.collectAsStateWithLifecycle()
                        HistoryScreen(days = mergeToday(history, state))
                    }
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        startSensors()
    }

    private fun startSensors() {
        val mode = sensors.start(stepTrackingAllowed = hasActivityRecognitionPermission())
        vm.onModeChanged(mode, sensors.hasStepCounter)
    }

    override fun onPause() {
        super.onPause()
        sensors.stop()
        vm.save()
        Log.d("StepLogger", "onPause: listeners unregistered, today saved")
    }

    private fun requestActivityRecognition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        }
    }

    private fun openAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        startActivity(intent)
    }

    private fun hasActivityRecognitionPermission(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.Q ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) ==
                    PackageManager.PERMISSION_GRANTED
}

object Routes {
    const val LIVE = "live"
    const val HISTORY = "history"
}