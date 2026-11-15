package com.example.stepandactivitylogger.sensors

import kotlin.math.sqrt

class StepDetector(
    var threshold: Float = 11.0f,
    private val minStepIntervalNs: Long = 300_000_000L,
    private val windowSize: Int = 5
) {
    private val window = mutableListOf<Float>()
    private var prev = 0f
    private var prevPrev = 0f
    private var lastStepTimeNs: Long? = null
    var lastSmoothed = 0f
        private set

    fun onSample(x: Float, y: Float, z: Float, timestampNs: Long): Boolean {
        val magnitude = sqrt(x * x + y * y + z * z)
        window.add(magnitude)
        if (window.size > windowSize) window.removeAt(0)
        val smoothed = window.average().toFloat()
        lastSmoothed = smoothed

        val isPeak = prev > prevPrev && prev >= smoothed && prev > threshold

        val last = lastStepTimeNs
        val farEnough = last == null || timestampNs - last >= minStepIntervalNs

        prevPrev = prev
        prev = smoothed

        if (isPeak && farEnough) {
            lastStepTimeNs = timestampNs
            return true
        }
        return false
    }

    fun reset() {
        window.clear()
        prev = 0f
        prevPrev = 0f
        lastStepTimeNs = null
        lastSmoothed = 0f
    }
}