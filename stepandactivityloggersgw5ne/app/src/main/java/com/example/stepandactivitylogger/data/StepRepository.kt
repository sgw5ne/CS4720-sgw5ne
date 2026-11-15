package com.example.stepandactivitylogger.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class StepRepository(private val dao: DayStepsDao) {

    fun todayKey(): String = LocalDate.now().toString()

    fun observeHistory(): Flow<List<DaySteps>> = dao.observeAll()

    fun observeDay(date: String): Flow<DaySteps?> = dao.observeDay(date)

    suspend fun getDay(date: String): DaySteps? = dao.getDay(date)

    suspend fun save(day: DaySteps) = dao.upsert(day)
}