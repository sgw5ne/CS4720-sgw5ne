package edu.virginia.cs4720.sgw5ne.collegefootballscores

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.util.stream.Collectors.toList

@Composable
fun ScoresRoute(viewModel: ScoresViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ScoresScreen(
        state = state,
        onWeekSelected = { viewModel.selectWeek(it) },
        onDivisionSelected = { viewModel.selectDivision(it) },
        onRefresh = { viewModel.refresh() },
        modifier = modifier
    )
}

@Composable
fun ScoresScreen(
    state: ScreenState,
    onWeekSelected: (Int) -> Unit,
    onDivisionSelected: (String) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "College Football",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onRefresh) { Text("Refresh") }
        }

        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("fbs", "fcs").forEach { division ->
                FilterChip(
                    selected = state.division == division,
                    onClick = { onDivisionSelected(division) },
                    label = { Text(division.uppercase())}
                )
            }
        }

        LazyRow(
            state = rememberLazyListState(initialFirstVisibleItemIndex = state.week - 1),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items((1..LAST_WEEK).toList()) { week ->
                FilterChip(
                    selected = state.week == week,
                    onClick = { onWeekSelected(week) },
                    label = { Text("Week $week") }
                )
            }
        }

        when (val scores = state.scores) {
            is ScoresUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ScoresUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(scores.message)
                    Button(onClick = onRefresh, modifier = Modifier.padding(top = 16.dp)) {
                        Text("Retry")
                    }
                }
            }
            is ScoresUiState.Success -> {
                if (scores.isRefreshing) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                if (scores.games.isEmpty()) {
                    Box(Modifier.fillMaxSize()) {
                        Text("No games this week")
                    }
                } else {
                    LazyColumn(contentPadding = PaddingValues(16.dp)) {
                        items(scores.games, key = { it.id }) { game ->
                            GameCard(game)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameCard(game: Game) {
    val isFinal = game.gameState == "final"
    val isLive = game.gameState == "live"

    val status = when {
        isFinal -> game.finalMessage.ifBlank { "FINAL" }
        isLive -> "LIVE · ${game.currentPeriod} · ${game.contestClock}"
        else -> "${game.startDate} · ${game.startTime}"
    }

    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                status,
                color = if (isLive) Color.Red else Color.Gray,
                style = MaterialTheme.typography.labelLarge
            )
            TeamRow("AWAY", game.awayName, game.awayScore, showScore = isFinal || isLive,
                isWinner = isFinal && game.awayWinner)
            TeamRow("HOME", game.homeName, game.homeScore, showScore = isFinal || isLive,
                isWinner = isFinal && game.homeWinner)
        }
    }
}

@Composable
fun TeamRow(label: String, name: String, score: String, showScore: Boolean, isWinner: Boolean) {
    val weight = if (isWinner) FontWeight.Bold else FontWeight.Normal
    Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
        Text(label, color = Color.Gray, modifier = Modifier.padding(end = 12.dp))
        Text(if (isWinner) "$name " else name, fontWeight = weight, modifier = Modifier.weight(1f))
        if (showScore) Text(score, fontWeight = weight)
    }
}
