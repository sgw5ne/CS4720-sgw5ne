package com.example.stepandactivitylogger.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

enum class StepMode {
    HARDWARE,
    ACCELEROMETER,
    NOT_ALLOWED,
    UNAVAILABLE
}

class StepSensorManager(
    context: Context,
    private val onAccelerometer: (x: Float, y: Float, z: Float, smoothed: Float) -> Unit,
    private val onStepDetected: () -> Unit,
    private val onCounterReading: (total: Long) -> Unit
) : SensorEventListener {
    private val sm: SensorManager? = context.getSystemService(SensorManager::class.java)

    private val accelerometer: Sensor? = sm?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val stepCounter: Sensor? = sm?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    val hasAccelerometer: Boolean get() = accelerometer != null
    val hasStepCounter: Boolean get() = stepCounter != null

    private val detector = StepDetector()
    var mode: StepMode = StepMode.UNAVAILABLE
        private set

    fun start(stepTrackingAllowed: Boolean): StepMode {
        detector.reset()

        mode = when {
            !stepTrackingAllowed -> StepMode.NOT_ALLOWED
            stepCounter != null -> StepMode.HARDWARE
            accelerometer != null -> StepMode.ACCELEROMETER
            else -> StepMode.UNAVAILABLE
        }

        accelerometer?.let {
            sm?.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }

        if (mode == StepMode.HARDWARE) {
            stepCounter?.let {
                sm?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            }
        }
        return mode
    }

    fun stop() {
        sm?.unregisterListener(this)
    }

    override fun onSensorChanged(e: SensorEvent) {
        when (e.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                val (x, y, z) = e.values
                val isStep = detector.onSample(x, y, z, e.timestamp)
                onAccelerometer(x, y, z, detector.lastSmoothed)
                if (isStep && mode == StepMode.ACCELEROMETER) onStepDetected()
            }

            Sensor.TYPE_STEP_COUNTER -> {
                onCounterReading(e.values[0].toLong())
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}