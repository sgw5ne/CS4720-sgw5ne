package com.example.stepandactivitylogger

import com.example.stepandactivitylogger.sensors.StepDetector
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.PI
import kotlin.math.sin

class StepDetectorTest {

    // SENSOR_DELAY_GAME ≈ 50 readings/s (Lecture 15) → one every 0.02 s = 20,000,000 ns
    private val sampleIntervalNs = 20_000_000L

    /** Feeds `seconds` of a gravity + sine-wave "bounce" into a detector; returns steps counted. */
    private fun simulate(bounceHz: Double, amplitude: Double, seconds: Int = 5): Int {
        val d = StepDetector()
        var steps = 0
        for (i in 0 until seconds * 50) {
            val t = i * 0.02
            val z = 9.8 + amplitude * sin(2 * PI * bounceHz * t)
            if (d.onSample(0f, 0f, z.toFloat(), i * sampleIntervalNs)) steps++
        }
        return steps
    }
    // Lecture 15 "Guess the activity": phone sitting on a desk → flat line at gravity
    @Test
    fun phoneOnDesk_countsNoSteps() {
        assertEquals(0, simulate(bounceHz = 0.0, amplitude = 0.0))
    }

    // Lecture 15 "Guess the activity": someone walking → ~2 steps per second
    @Test
    fun normalWalking_twoStepsPerSecond_countsTenInFiveSeconds() {
        assertEquals(10, simulate(bounceHz = 2.0, amplitude = 2.5))
    }

    // Lecture 15 "Guess the activity": someone shaking the phone → peaks every 0.25 s.
    // The 0.3 s minimum gap ("at least ~0.3 s apart") caps the count well below 1 per peak.
    @Test
    fun fastShaking_isCappedByMinimumStepGap() {
        val steps = simulate(bounceHz = 4.0, amplitude = 4.0)   // 20 raw peaks in 5 s
        assertTrue("got $steps", steps <= 17)                  // 5 s / 0.3 s ≈ 16.7 max
    }

    // Documents a LIMITATION for the README: very gentle walking never crosses 11 m/s².
    @Test
    fun veryGentleWalking_staysUnderThreshold_isMissed() {
        assertEquals(0, simulate(bounceHz = 2.0, amplitude = 0.8))
    }
}