package com.example.stepandactivitylogger

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stepandactivitylogger.data.DaySteps
import com.example.stepandactivitylogger.sensors.StepMode
import java.nio.file.Files.size
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.forEachIndexed

fun mergeToday(history: List<DaySteps>, state: StepUiState): List<DaySteps> {
    val counting = state.mode == StepMode.HARDWARE || state.mode == StepMode.ACCELEROMETER
    if (!counting || state.todayDate.isEmpty()) return history
    return listOf(DaySteps(date = state.todayDate, steps = state.todaySteps)) +
            history.filter { it.date != state.todayDate }
}

@Composable
fun HistoryScreen(days: List<DaySteps>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Spacer(Modifier.height(16.dp))
            Text("History")
        }
        if (days.isEmpty()) {
            item {
                Text(
                    "No steps recorded yet."
                )
            }
        } else {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Last 7 days")
                        Spacer(Modifier.height(12.dp))
                        StepsBarChart(days = days.take(7).reversed())
                    }
                }
            }
            items(days, key = { it.date }) { day ->
                DayRow(day)
                HorizontalDivider()
            }
        }
    }
}

private val rowFormat = DateTimeFormatter.ofPattern("EEE, MMM d")

@Composable
private fun DayRow(day: DaySteps) {
    val isToday = day.date == LocalDate.now().toString()
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(if (isToday) "Today" else LocalDate.parse(day.date).format(rowFormat))
        Text("${day.steps} steps")
    }
}

@Composable
private fun StepsBarChart(days: List<DaySteps>) {
    val barColor = MaterialTheme.colorScheme.primary
    val maxSteps = days.maxOf { it.steps }.coerceAtLeast(1)
    Canvas(modifier = Modifier.fillMaxWidth().height(160.dp)) {
        val slot = size.width / days.size
        val barWidth = slot * 0.6f

        days.forEachIndexed { i, day ->
            val barHeight = size.height * day.steps / maxSteps
            drawRect(
                color = barColor,
                topLeft = Offset(x = i * slot + (slot - barWidth) / 2, y = size.height - barHeight),
                size = Size(width = barWidth, height = barHeight)
            )
        }
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        days.forEach { day ->
            Text(
                LocalDate.parse(day.date).dayOfWeek.name.take(3),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryPreview() {
    val today = LocalDate.now()
    HistoryScreen(
        days = listOf(4200, 6100, 3500, 8000, 7200, 2900, 5600).mapIndexed { i, steps ->
            DaySteps(date = today.minusDays(i.toLong()).toString(), steps = steps)
        }
    )
}