package com.example.stepandactivitylogger.sensors

import com.example.stepandactivitylogger.data.DaySteps

object StepMath {
    fun applyCounterReading(today: DaySteps?, date: String, counter: Long): DaySteps {
        if (today == null) return DaySteps(date = date, steps = 0, lastCounter = counter)
        val last = today.lastCounter ?: return today.copy(lastCounter = counter)
        val delta = if (counter >= last) counter - last else counter
        return today.copy(steps = today.steps + delta.toInt(), lastCounter = counter)
    }

    fun applyDetectedStep(today: DaySteps?, date: String): DaySteps =
        today?.copy(steps = today.steps + 1) ?: DaySteps(date = date, steps = 1)
}