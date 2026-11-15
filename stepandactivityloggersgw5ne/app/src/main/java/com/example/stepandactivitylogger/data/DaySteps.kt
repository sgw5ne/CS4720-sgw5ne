package com.example.stepandactivitylogger.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_steps")
data class DaySteps(
    @PrimaryKey val date: String,
    val steps: Int = 0,
    val counterBaseline: Long? = null
)
