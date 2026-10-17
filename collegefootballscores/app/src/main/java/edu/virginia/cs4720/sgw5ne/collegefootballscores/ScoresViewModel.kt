package edu.virginia.cs4720.sgw5ne.collegefootballscores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.platform.android.AndroidLogHandler.publish
import java.lang.System.load
import java.util.Calendar
import java.util.TimeZone
import kotlin.coroutines.cancellation.CancellationException

sealed interface ScoresUiState {
    data object Loading : ScoresUiState
    data class Success(val games: List<Game>, val isRefreshing: Boolean) : ScoresUiState
    data class Error(val message: String) : ScoresUiState
}

data class ScreenState(
    val week: Int,
    val division: String,
    val scores: ScoresUiState = ScoresUiState.Loading
)

class ScoresViewModel(private val repository: ScoresRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ScreenState(week = currentWeek(), division = "fbs"))
    val uiState: StateFlow<ScreenState> = _uiState.asStateFlow()

    private var savedGames: List<Game>? = null
    private var isFetching = false
    private var fetchFailed = false
    private var waitingForDb = false

    private var dbJob: Job? = null
    private var fetchJob: Job? = null

    init { load() }

    fun selectWeek(week: Int) {
        _uiState.value = _uiState.value.copy(week = week)
        load()
    }

    fun selectDivision(division: String) {
        _uiState.value = _uiState.value.copy(division = division)
        load()
    }

    fun refresh() {
        fetch()
    }

    private fun load() {
        savedGames = null
        fetchFailed = false
        waitingForDb = false
        val state = _uiState.value

        dbJob?.cancel()
        dbJob = viewModelScope.launch {
            repository.observeWeek(state.division, state.week).collect { freshDatabaseList ->
                savedGames = freshDatabaseList
                if (freshDatabaseList.isNotEmpty()) waitingForDb = false
                publish()
            }
        }
        fetch()
    }

    private fun fetch() {
        fetchJob?.cancel()
        val state = _uiState.value
        fetchJob = viewModelScope.launch {
            isFetching = true
            publish()
            try {
                val count = repository.refresh(state.division, state.week)
                fetchFailed = false
                waitingForDb = count > 0 && savedGames.isNullOrEmpty()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                fetchFailed = true
            }
            isFetching = false
            publish()
        }
    }

    private fun publish() {
        val games = savedGames
        val scores = when {
            !games.isNullOrEmpty() -> ScoresUiState.Success(games, isRefreshing = isFetching)
            games == null || isFetching || waitingForDb -> ScoresUiState.Loading
            fetchFailed -> ScoresUiState.Error("Couldn't load score. Check your connection and try again.")
            else -> ScoresUiState.Success(emptyList(), isRefreshing = false)
        }
        _uiState.value = _uiState.value.copy(scores = scores)
    }

    companion object {
        fun factory(repository: ScoresRepository) = viewModelFactory {
            initializer { ScoresViewModel(repository)}
        }
    }
}

fun currentWeek(): Int {
    val week1 = Calendar.getInstance(TimeZone.getTimeZone("America/New_York")).apply {
        clear()
        set(2026, Calendar.AUGUST, 31)
    }.timeInMillis
    val oneWeek = 7L * 24 * 60 * 60 * 1000
    val week = ((System.currentTimeMillis() - week1) / oneWeek + 1).toInt()
    return week.coerceIn(1, LAST_WEEK)
}

const val LAST_WEEK = 17