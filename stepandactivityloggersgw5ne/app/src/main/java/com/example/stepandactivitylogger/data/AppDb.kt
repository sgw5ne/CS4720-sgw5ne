package com.example.stepandactivitylogger.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DaySteps::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun dayStepsDao(): DayStepsDao

    companion object {
        @Volatile private var INSTANCE: AppDb? = null

        fun get(context: Context): AppDb {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext, AppDb::class.java, "steplogger.db"
                ).build()
            }
            return INSTANCE!!
        }
    }
}