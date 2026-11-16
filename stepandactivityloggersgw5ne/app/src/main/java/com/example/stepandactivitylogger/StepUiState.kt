package com.example.stepandactivitylogger

import com.example.stepandactivitylogger.sensors.StepMode

data class StepUiState(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f,
    val smoothed : Float = 0f,
    val todaySteps: Int = 0,
    val mode: StepMode = StepMode.UNAVAILABLE,
    val hasStepCounter: Boolean = false,
    val permissionPermanentlyDenied: Boolean = false
    val todayDate: String = ""
)
