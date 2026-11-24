package edu.virginia.cs4720.sgw5ne.finalproject

import androidx.annotation.StringRes
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.virginia.cs4720.sgw5ne.finalproject.data.Event
import edu.virginia.cs4720.sgw5ne.finalproject.data.Group
import edu.virginia.cs4720.sgw5ne.finalproject.ui.theme.FinalprojectTheme

/** Which result sections the All / Groups / Events filter shows. */
enum class SearchFilter(@StringRes val labelRes: Int) {
    ALL(R.string.search_filter_all),
    GROUPS(R.string.tab_groups),
    EVENTS(R.string.search_filter_events),
}

/**
 * Search: a search field, an All / Groups / Events filter, then two result sections,
 * "Groups to discover" and "Upcoming events" (as in the wireframe).
 * The query is reported through [onQueryChange] so a ViewModel can run the Firestore search later.
 */
@Composable
fun SearchScreen(
    groups: List<Group>,
    events: List<Event>,
    onQueryChange: (String) -> Unit,
    onGroupClick: (Group) -> Unit,
    onEventClick: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    // rememberSaveable so the query and filter survive rotation.
    var query by rememberSaveable { mutableStateOf("") }
    var filter by rememberSaveable { mutableStateOf(SearchFilter.ALL) }

    val showGroups = filter != SearchFilter.EVENTS && groups.isNotEmpty()
    val showEvents = filter != SearchFilter.GROUPS && events.isNotEmpty()

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.tab_search),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 16.dp)
                .semantics { heading() }
        )

        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                onQueryChange(it)
            },
            placeholder = { Text(stringResource(R.string.search_hint)) },
            // Decorative: the field's placeholder/label already says what it's for.
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(12.dp))

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            SearchFilter.entries.forEachIndexed { index, option ->
                SegmentedButton(
                    selected = filter == option,
                    onClick = { filter = option },
                    shape = SegmentedButtonDefaults.itemShape(index, SearchFilter.entries.size)
                ) {
                    Text(stringResource(option.labelRes))
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        if (!showGroups && !showEvents) {
            EmptySearch(hasQuery = query.isNotBlank(), modifier = Modifier.fillMaxSize())
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (showGroups) {
                    item(key = "groups-header") { SectionHeader(R.string.search_groups_to_discover) }
                    items(groups, key = { "group-${it.id}" }) { group ->
                        DiscoverGroupCard(group = group, onClick = { onGroupClick(group) })
                    }
                }
                if (showEvents) {
                    item(key = "events-header") { SectionHeader(R.string.search_upcoming_events) }
                    items(events, key = { "event-${it.id}" }) { event ->
                        EventCard(event = event, onClick = { onEventClick(event) })
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(@StringRes textRes: Int) {
    Text(
        text = stringResource(textRes),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .padding(top = 8.dp, start = 8.dp)
            .semantics { heading() }
    )
}

/** A group you could join: name on the left, size and visibility on the right. */
@Composable
private fun DiscoverGroupCard(group: Group, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = group.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(12.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = pluralStringResource(R.plurals.groups_member_count, group.memberCount, group.memberCount),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(if (group.isPublic) R.string.groups_public else R.string.groups_private),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun EmptySearch(hasQuery: Boolean, modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.padding(32.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(if (hasQuery) R.string.search_no_results_title else R.string.search_empty_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(if (hasQuery) R.string.search_no_results_body else R.string.search_empty_body),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Sample data is for previews only; the real app will search Firestore.
private val previewGroups = listOf(
    Group("1", "Fun Group 1", 13, isPublic = true),
    Group("2", "Mobile Apps", 24, isPublic = true),
)
private val previewEvents = listOf(
    Event("1", "Event Title", "Fun Group 1", "Campus Library", "Oct. 26", "15:30"),
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SearchScreenPreview() {
    FinalprojectTheme {
        Scaffold(
            bottomBar = { AppBottomBar(currentTab = TopLevelTab.SEARCH, onTabSelected = {}) }
        ) { padding ->
            SearchScreen(
                groups = previewGroups,
                events = previewEvents,
                onQueryChange = {},
                onGroupClick = {},
                onEventClick = {},
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchScreenEmptyDarkPreview() {
    FinalprojectTheme(darkTheme = true) {
        Scaffold(
            bottomBar = { AppBottomBar(currentTab = TopLevelTab.SEARCH, onTabSelected = {}) }
        ) { padding ->
            SearchScreen(
                groups = emptyList(),
                events = emptyList(),
                onQueryChange = {},
                onGroupClick = {},
                onEventClick = {},
                modifier = Modifier.padding(padding)
            )
        }
    }
}
