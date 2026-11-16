package com.example.stepandactivitylogger

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stepandactivitylogger.sensors.StepMode

@Composable
fun LiveScreen(state: StepUiState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Live sensors")
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Accelerometer")
                Spacer(Modifier.height(8.dp))
                AxisRow("x", state.x)
                AxisRow("y", state.y)
                AxisRow("z", state.z)
                Spacer(Modifier.height(8.dp))
                Text("Smoothed magnitude: %.2f".format(state.smoothed))
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Today's steps")
                when (state.mode) {
                    StepMode.HARDWARE, StepMode.ACCELEROMETER -> {
                        Text("${state.todaySteps}")
                    }
                    StepMode.NOT_ALLOWED, StepMode.UNAVAILABLE -> {
                        Text("-")
                    }
                }
                Text(modeDescription(state.mode))
            }
        }
    }
}

private fun modeDescription(mode: StepMode): String = when (mode) {
    StepMode.HARDWARE -> "Counting with this device's step counter"
    StepMode.ACCELEROMETER -> "No step counter, counted with accelerometer"
    StepMode.NOT_ALLOWED -> "Step counting is off, permissions required"
    StepMode.UNAVAILABLE -> "This device cannot count steps"
}

@Composable
private fun AxisRow(label: String, value: Float) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label)
        Text("%.2f".format(value))
    }
}

@Preview(showBackground = true)
@Composable
private fun LiveScreenPreview() {
    LiveScreen(
        StepUiState(x = 0.12f, y = 9.71f, z = 0.43f, smoothed = 9.80f,
            todaySteps = 1234, mode = StepMode.ACCELEROMETER)
    )
}