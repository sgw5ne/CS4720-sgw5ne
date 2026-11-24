package edu.virginia.cs4720.sgw5ne.finalproject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.virginia.cs4720.sgw5ne.finalproject.data.Event
import edu.virginia.cs4720.sgw5ne.finalproject.ui.theme.FinalprojectTheme

/**
 * Profile: "@username" with a Settings button, then the user's own schedule
 * grouped by date (date header, then that day's events with time range and visibility).
 * Events still waiting on an organizer are tagged "Pending approval", which is where
 * Home's "Pending approvals" button leads.
 */
@Composable
fun ProfileScreen(
    username: String?,
    events: List<Event>,
    onSettingsClick: () -> Unit,
    onEventClick: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    // Keep the order events arrive in (Firestore query will sort by start time).
    val eventsByDate = events.groupBy { it.date }

    Column(modifier = modifier.fillMaxSize()) {
        // Header row: username on the left, settings on the right (as in the wireframe).
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 16.dp, top = 24.dp, bottom = 16.dp)
        ) {
            Text(
                text = if (username.isNullOrBlank()) {
                    stringResource(R.string.tab_profile)
                } else {
                    stringResource(R.string.profile_handle, username)
                },
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f).semantics { heading() }
            )
            // IconButtons are 48dp touch targets by default; icon-only, so it needs a description.
            FilledTonalIconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = stringResource(R.string.profile_settings)
                )
            }
        }

        Text(
            text = stringResource(R.string.profile_my_schedule),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 24.dp).semantics { heading() }
        )

        Spacer(Modifier.height(8.dp))

        if (events.isEmpty()) {
            EmptySchedule(modifier = Modifier.fillMaxSize())
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                eventsByDate.forEach { (date, dayEvents) ->
                    item(key = "date-$date") {
                        DaySection(date = date, events = dayEvents, onEventClick = onEventClick)
                    }
                }
            }
        }
    }
}

/** One day: a date banner with that day's events inside (the large box in the wireframe). */
@Composable
private fun DaySection(
    date: String,
    events: List<Event>,
    onEventClick: (Event) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(12.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = date,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .semantics { heading() }
                )
            }
            events.forEach { event ->
                ScheduleEventCard(event = event, onClick = { onEventClick(event) })
            }
        }
    }
}

@Composable
private fun ScheduleEventCard(event: Event, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = if (event.endTime.isBlank()) event.time
                    else stringResource(R.string.profile_time_range, event.time, event.endTime),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (event.visibility.isNotBlank()) {
                    Text(
                        text = event.visibility,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (event.isPendingApproval) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = stringResource(R.string.profile_pending_approval),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptySchedule(modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.padding(32.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.profile_empty_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.profile_empty_body),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Sample data is for previews only; the real app will load events from Firestore.
private val previewSchedule = listOf(
    Event("1", "Mobile Apps", "Mobile Apps", "Campus Library, Room 2", "Tuesday Oct. 26", "15:30", "16:45", "Public"),
    Event("2", "Board game night", "Fun Group 1", "Newcomb Hall", "Tuesday Oct. 26", "19:00", "21:00", "Private", isPendingApproval = true),
    Event("3", "Morning run", "Fun Group 1", "Rotunda steps", "Thursday Oct. 28", "07:00", "08:00", "Public"),
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileScreenPreview() {
    FinalprojectTheme {
        Scaffold(
            bottomBar = { AppBottomBar(currentTab = TopLevelTab.PROFILE, onTabSelected = {}) }
        ) { padding ->
            ProfileScreen(
                username = "lcgwilliam",
                events = previewSchedule,
                onSettingsClick = {},
                onEventClick = {},
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProfileScreenEmptyDarkPreview() {
    FinalprojectTheme(darkTheme = true) {
        Scaffold(
            bottomBar = { AppBottomBar(currentTab = TopLevelTab.PROFILE, onTabSelected = {}) }
        ) { padding ->
            ProfileScreen(
                username = null,
                events = emptyList(),
                onSettingsClick = {},
                onEventClick = {},
                modifier = Modifier.padding(padding)
            )
        }
    }
}
