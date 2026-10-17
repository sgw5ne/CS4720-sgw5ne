package edu.virginia.cs4720.sgw5ne.collegefootballscores

import kotlinx.coroutines.flow.Flow

const val SEASON = 2026

class ScoresRepository(private val api: NcaaApi, private val dao: GameDao) {
    fun observeWeek(division: String, week: Int): Flow<List<Game>> {
        return dao.observeWeek(division, week)
    }

    suspend fun refresh(division: String, week: Int): Int {
        val response = api.getScoreboard(division, SEASON, week.toString().padStart(2, '0'))
        val games = response.games.map { it.game.toGame(division, week) }
        dao.insertAll(games)
        return games.size
    }
}

private fun GameJson.toGame(division: String, week: Int) = Game(
    id = "${division}_$gameID",
    division = division,
    week = week,
    awayName = away.names.short,
    awayScore = away.score,
    awayWinner = away.winner,
    homeName = home.names.short,
    homeScore = home.score,
    homeWinner = home.winner,
    gameState = gameState,
    startDate = startDate,
    startTime = startTime,
    startEpoch = startTimeEpoch.toLongOrNull() ?: 0L,
    currentPeriod = currentPeriod,
    contestClock = contestClock,
    finalMessage = finalMessage
)