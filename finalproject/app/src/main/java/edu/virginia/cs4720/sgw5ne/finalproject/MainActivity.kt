package edu.virginia.cs4720.sgw5ne.finalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import edu.virginia.cs4720.sgw5ne.finalproject.ui.theme.FinalprojectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinalprojectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // App starts at the Log In Menu (required). Navigation comes later.
                    LoginMenuScreen(
                        onLogInClick = { /* TODO: navigate to Log In */ },
                        onSignUpClick = { /* TODO: navigate to Sign Up */ },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
