package edu.virginia.cs4720.sgw5ne.finalproject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.virginia.cs4720.sgw5ne.finalproject.data.Group
import edu.virginia.cs4720.sgw5ne.finalproject.ui.theme.FinalprojectTheme

/**
 * Groups: the groups you belong to. Each card shows the group name, member count and
 * public/private, with a "Members" button; a "New group" button sits bottom-right.
 */
@Composable
fun GroupsScreen(
    groups: List<Group>,
    onGroupClick: (Group) -> Unit,
    onMembersClick: (Group) -> Unit,
    onNewGroupClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            // Coral: creating a group is a "get together" action in this theme.
            ExtendedFloatingActionButton(
                onClick = onNewGroupClick,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) }, // label says it
                text = { Text(stringResource(R.string.groups_new_group)) },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        },
        // The outer app Scaffold already handles system bar insets.
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Text(
                text = stringResource(R.string.tab_groups),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 16.dp)
                    .semantics { heading() }
            )

            if (groups.isEmpty()) {
                EmptyGroups(modifier = Modifier.fillMaxSize())
            } else {
                LazyColumn(
                    // Extra bottom padding so the last card can scroll clear of the FAB.
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(groups, key = { it.id }) { group ->
                        GroupCard(
                            group = group,
                            onClick = { onGroupClick(group) },
                            onMembersClick = { onMembersClick(group) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GroupCard(
    group: Group,
    onClick: () -> Unit,
    onMembersClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(
                        R.string.groups_count_and_visibility,
                        pluralStringResource(R.plurals.groups_member_count, group.memberCount, group.memberCount),
                        stringResource(if (group.isPublic) R.string.groups_public else R.string.groups_private)
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.width(12.dp))
            // Separate, smaller action from the card's own tap; still a 48dp touch target.
            OutlinedButton(onClick = onMembersClick) {
                Text(stringResource(R.string.groups_members))
            }
        }
    }
}

@Composable
private fun EmptyGroups(modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.padding(32.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.groups_empty_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.groups_empty_body),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Sample data is for previews only; the real app will load groups from Firestore.
private val previewGroups = listOf(
    Group("me", "Me", 1, isPublic = false),
    Group("1", "Fun Group 1", 13, isPublic = true),
    Group("2", "Mobile Apps", 24, isPublic = true),
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun GroupsScreenPreview() {
    FinalprojectTheme {
        Scaffold(
            bottomBar = { AppBottomBar(currentTab = TopLevelTab.GROUPS, onTabSelected = {}) }
        ) { padding ->
            GroupsScreen(
                groups = previewGroups,
                onGroupClick = {},
                onMembersClick = {},
                onNewGroupClick = {},
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun GroupsScreenEmptyDarkPreview() {
    FinalprojectTheme(darkTheme = true) {
        Scaffold(
            bottomBar = { AppBottomBar(currentTab = TopLevelTab.GROUPS, onTabSelected = {}) }
        ) { padding ->
            GroupsScreen(
                groups = emptyList(),
                onGroupClick = {},
                onMembersClick = {},
                onNewGroupClick = {},
                modifier = Modifier.padding(padding)
            )
        }
    }
}
