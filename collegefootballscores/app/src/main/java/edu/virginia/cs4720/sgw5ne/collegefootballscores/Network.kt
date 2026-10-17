package edu.virginia.cs4720.sgw5ne.collegefootballscores

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

@Serializable
data class ScoreboardResponse(val games: List<GameWrapper> = emptyList())

@Serializable
data class GameWrapper(val game: GameJson)

@Serializable
data class GameJson(
    val gameID: String,
    val away: TeamJson,
    val home: TeamJson,
    val gameState: String = "",
    val startDate: String = "",
    val startTime: String = "",
    val startTimeEpoch: String = "",
    val currentPeriod: String = "",
    val contestClock: String = "",
    val finalMessage: String = ""
)

@Serializable
data class TeamJson(
    val score: String = "",
    val names: TeamNames = TeamNames(),
    val winner: Boolean = false
)

@Serializable
data class TeamNames(val short: String = "")

interface NcaaApi {
    @GET("scoreboard/football/{division}/{year}/{week}")
    suspend fun getScoreboard(
        @Path("division") division: String,
        @Path("year") year: Int,
        @Path("week") week: String
    ): ScoreboardResponse
}

object RetrofitClient {
    private val json = Json { ignoreUnknownKeys = true }

    val api: NcaaApi = Retrofit.Builder()
        .baseUrl("https://ncaa-api.henrygd.me/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(NcaaApi::class.java)
}