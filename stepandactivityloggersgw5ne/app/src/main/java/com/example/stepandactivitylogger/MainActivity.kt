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
import java.util.jar.Manifest

class MainActivity : ComponentActivity() {

    private lateinit var repository : StepRepository
    private lateinit var vm: StepViewModel
    private lateinit var sensors: StepSensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = StepRepository(AppDb.get(applicationContext).dayStepsDao())
        vm = ViewModelProvider(this, StepViewModel.factory(repository))[StepViewModel::class.java]

        val db = AppDb.get(applicationContext)
        val dao = db.dayStepsDao()
        repository = StepRepository(dao)

        sensors = StepSensorManager(
            context = applicationContext,
            onAccelerometer = { x, y, xz, smoothed -> vm.onAccelerometer(x, y, z, smoothed) },
            onStepDetected = { vm.onStepDetected() },
            onCounterReading = { total -> vm.onCounterReading(total) }
        )
        setContent {
            StepandactivityloggerTheme {
                val state by vm.uiState.collectAsStateWithLifecycle()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LiveScreen(state = state, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val mode = sensors.start(stepTrackingAllowed = hasActivityRecognitionPermission())
        vm.onModeChanged(mode, sensors.hasStepCounter)
        Log.d("StepLogger", "onResume: listeners registered, mode=$mode")
    }

    override fun onPause() {
        super.onPause()
        sensors.stop()
        vm.save()
        Log.d("StepLogger", "onPause: listeners unregistered, today saved")
    }

    private fun hasActivityRecognitionPermission(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.Q ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) ==
                    PackageManager.PERMISSION_GRANTED
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StepandactivityloggerTheme {
        Greeting("Android")
    }
}