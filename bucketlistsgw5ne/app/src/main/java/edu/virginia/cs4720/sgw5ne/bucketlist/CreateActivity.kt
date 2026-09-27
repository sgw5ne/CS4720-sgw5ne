package edu.virginia.cs4720.sgw5ne.bucketlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class CreateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { YourTheme {
            CreateScreen(onDone = { finish() })
        }}
    }
}