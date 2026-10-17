package edu.virginia.cs4720.sgw5ne.collegefootballscores

import android.app.Application
import androidx.room.Room

class ScoresApp : Application() {
    lateinit var repository: ScoresRepository
    override fun onCreate() {
        super.onCreate()
        val db = Room.databaseBuilder(this, AppDb::class.java, "scores.db").build()
        repository = ScoresRepository(RetrofitClient.api, db.gameDao())
    }
}