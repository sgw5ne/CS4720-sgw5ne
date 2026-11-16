package com.example.stepandactivitylogger

import com.example.stepandactivitylogger.data.DaySteps
import com.example.stepandactivitylogger.sensors.StepMath
import org.junit.Assert.assertEquals
import org.junit.Test

class StepMathTest {

    private val date = "2026-11-15"

    @Test
    fun firstReadingOfTheDay_isTheStartingValue_withZeroSteps() {
        val day = StepMath.applyCounterReading(today = null, date = date, counter = 18_250)
        assertEquals(0, day.steps)
        assertEquals(18_250L, day.lastCounter)
    }

    @Test
    fun laterReading_addsOnlyTheDifference() {
        val start = StepMath.applyCounterReading(null, date, 18_250)
        val later = StepMath.applyCounterReading(start, date, 18_300)
        assertEquals(50, later.steps)
    }

    @Test
    fun phoneRebootedMidDay_counterRestartsAtZero_keepsEarlierSteps() {
        val beforeReboot = DaySteps(date = date, steps = 500, lastCounter = 20_000)
        val afterReboot = StepMath.applyCounterReading(beforeReboot, date, counter = 30)
        assertEquals(530, afterReboot.steps)          // 500 kept + 30 since reboot
        assertEquals(30L, afterReboot.lastCounter)
    }

    @Test
    fun permissionGrantedMidDay_existingStepsAreKept() {
        val noCounterYet = DaySteps(date = date, steps = 120, lastCounter = null)
        val first = StepMath.applyCounterReading(noCounterYet, date, 5_000)
        val next = StepMath.applyCounterReading(first, date, 5_010)
        assertEquals(120, first.steps)                // no jump when the counter first appears
        assertEquals(130, next.steps)
    }

    @Test
    fun accelerometerSteps_startAtOne_thenAddOne() {
        val first = StepMath.applyDetectedStep(today = null, date = date)
        val second = StepMath.applyDetectedStep(first, date)
        assertEquals(1, first.steps)
        assertEquals(2, second.steps)
    }
}