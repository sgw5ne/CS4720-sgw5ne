package com.example.stepandactivitylogger.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DayStepsDao {
    @Query("SELECT * FROM daily_steps ORDER BY date DESC")
    fun observeAll(): Flow<List<DaySteps>>

    @Query("SELECT * FROM daily_steps WHERE date = :date")
    fun observeDay(date: String): Flow<DaySteps?>

    @Query("SELECT * FROM daily_steps WHERE date = :date")
    suspend fun getDay(date: String): DaySteps?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(day: DaySteps)
}