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
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.virginia.cs4720.sgw5ne.finalproject.data.Event
import edu.virginia.cs4720.sgw5ne.finalproject.ui.theme.FinalprojectTheme

/**
 * Home: "Upcoming Events". A scrolling list of event cards (title, group @ location,
 * date + time), with a "Pending approvals" shortcut in the bottom-start corner.
 * The bottom bar is supplied by the app scaffold, not this screen.
 */
@Composable
fun HomeScreen(
    events: List<Event>,
    pendingApprovalCount: Int,
    onEventClick: (Event) -> Unit,
    onPendingApprovalsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        // Wireframe places the pending-approvals shortcut bottom-left, just above the nav bar.
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.Start,
        floatingActionButton = {
            PendingApprovalsButton(
                count = pendingApprovalCount,
                onClick = onPendingApprovalsClick
            )
        },
        // The outer app Scaffold already handles system bar insets.
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0)
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Text(
                text = stringResource(R.string.home_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 16.dp)
            )

            if (events.isEmpty()) {
                EmptyEvents(modifier = Modifier.fillMaxSize())
            } else {
                LazyColumn(
                    // Extra bottom padding so the last card can scroll clear of the FAB.
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(events, key = { it.id }) { event ->
                        EventCard(event = event, onClick = { onEventClick(event) })
                    }
                }
            }
        }
    }
}

/** One event in the list. The whole card is the touch target. */
@Composable
private fun EventCard(
    event: Event,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            // Left: what + who/where
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.home_event_group_at_location, event.groupName, event.location),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.width(12.dp))

            // Right: when (date over time, as in the wireframe)
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                DateTimeChip(text = event.date)
                DateTimeChip(text = event.time)
            }
        }
    }
}

@Composable
private fun DateTimeChip(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun PendingApprovalsButton(count: Int, onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        BadgedBox(
            badge = {
                if (count > 0) {
                    Badge { Text(count.toString()) }
                }
            }
        ) {
            Text(
                text = stringResource(R.string.home_pending_approvals),
                // Leave room for the badge so it doesn't cover the label.
                modifier = Modifier.padding(end = if (count > 0) 12.dp else 0.dp)
            )
        }
    }
}

@Composable
private fun EmptyEvents(modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.padding(32.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.home_empty_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.home_empty_body),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Sample data is for previews only; the real app will load events from Firestore.
private val previewEvents = listOf(
    Event("1", "Mobile Apps study session", "Mobile Apps", "Campus Library, Room 2", "Oct. 26", "15:30"),
    Event("2", "Board game night", "Fun Group 1", "Newcomb Hall", "Oct. 28", "19:00"),
    Event("3", "Morning run", "Fun Group 1", "Rotunda steps", "Oct. 30", "07:00"),
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    FinalprojectTheme {
        Scaffold(
            bottomBar = { AppBottomBar(currentTab = TopLevelTab.HOME, onTabSelected = {}) }
        ) { padding ->
            HomeScreen(
                events = previewEvents,
                pendingApprovalCount = 2,
                onEventClick = {},
                onPendingApprovalsClick = {},
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenEmptyDarkPreview() {
    FinalprojectTheme(darkTheme = true) {
        Scaffold(
            bottomBar = { AppBottomBar(currentTab = TopLevelTab.HOME, onTabSelected = {}) }
        ) { padding ->
            HomeScreen(
                events = emptyList(),
                pendingApprovalCount = 0,
                onEventClick = {},
                onPendingApprovalsClick = {},
                modifier = Modifier.padding(padding)
            )
        }
    }
}
