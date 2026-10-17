package edu.virginia.cs4720.sgw5ne.collegefootballscores

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity
data class Game(
    @PrimaryKey val id: String,
    val division: String,
    val week: Int,
    val awayName: String,
    val awayScore: String,
    val awayWinner: Boolean,
    val homeName: String,
    val homeScore: String,
    val homeWinner: Boolean,
    val gameState: String,
    val startDate: String,
    val startTime: String,
    val startEpoch: Long,
    val currentPeriod: String,
    val contestClock: String,
    val finalMessage: String
)

@Dao
interface GameDao {
    @Query("SELECT * FROM Game WHERE division = :division AND week = :week ORDER BY startEpoch")
    fun observeWeek(division: String, week: Int): Flow<List<Game>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<Game>)
}

@Database(entities = [Game::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun gameDao(): GameDao
}
