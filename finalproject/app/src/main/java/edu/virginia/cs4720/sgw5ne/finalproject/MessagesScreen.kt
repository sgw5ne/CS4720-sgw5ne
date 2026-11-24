package edu.virginia.cs4720.sgw5ne.finalproject

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.virginia.cs4720.sgw5ne.finalproject.data.Conversation
import edu.virginia.cs4720.sgw5ne.finalproject.ui.theme.FinalprojectTheme

/**
 * Messages: a list of group chats and DMs. Each row shows an avatar, the chat name,
 * the latest message, its time, and an unread-count badge (as in the wireframe).
 */
@Composable
fun MessagesScreen(
    conversations: List<Conversation>,
    onConversationClick: (Conversation) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.tab_messages),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 8.dp)
                .semantics { heading() }
        )

        if (conversations.isEmpty()) {
            EmptyMessages(modifier = Modifier.fillMaxSize())
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(conversations, key = { it.id }) { conversation ->
                    ConversationRow(
                        conversation = conversation,
                        onClick = { onConversationClick(conversation) }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 88.dp), // lines up with the text, not the avatar
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }
    }
}

/** A full-width tappable row (ListItem is >= 72dp tall, well over the 48dp minimum). */
@Composable
private fun ConversationRow(
    conversation: Conversation,
    onClick: () -> Unit
) {
    val hasUnread = conversation.unreadCount > 0
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
        leadingContent = { Avatar(name = conversation.name) },
        headlineContent = {
            Text(
                text = conversation.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            Text(
                text = conversation.lastMessage,
                style = MaterialTheme.typography.bodyMedium,
                // Unread chats get bolder preview text so they stand out at a glance.
                fontWeight = if (hasUnread) FontWeight.SemiBold else FontWeight.Normal,
                color = if (hasUnread) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = conversation.lastMessageTime,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (hasUnread) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (hasUnread) {
                    val unreadDescription = pluralStringResource(
                        R.plurals.messages_unread_description,
                        conversation.unreadCount,
                        conversation.unreadCount
                    )
                    Badge(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        // TalkBack says "3 unread messages" instead of just "3".
                        modifier = Modifier.clearAndSetSemantics { contentDescription = unreadDescription }
                    ) {
                        Text(conversation.unreadCount.toString())
                    }
                }
            }
        }
    )
}

/**
 * Initials in a circle. Decorative: the name is already read next to it,
 * so it's hidden from TalkBack to avoid reading the name twice.
 */
@Composable
private fun Avatar(name: String) {
    val initials = name
        .removePrefix("@")
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clearAndSetSemantics { }
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun EmptyMessages(modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.padding(32.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.messages_empty_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.messages_empty_body),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Sample data is for previews only; the real app will load chats from Firestore.
private val previewConversations = listOf(
    Conversation("1", "Fun Group 1", "See you at the event!", "15:20", unreadCount = 1),
    Conversation("2", "Mobile Apps", "The room is confirmed.", "14:05", unreadCount = 3),
    Conversation("3", "@lcgwilliam", "Thanks for the invite.", "Yesterday"),
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MessagesScreenPreview() {
    FinalprojectTheme {
        Scaffold(
            bottomBar = { AppBottomBar(currentTab = TopLevelTab.MESSAGES, onTabSelected = {}) }
        ) { padding ->
            MessagesScreen(
                conversations = previewConversations,
                onConversationClick = {},
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MessagesScreenDarkPreview() {
    FinalprojectTheme(darkTheme = true) {
        Scaffold(
            bottomBar = { AppBottomBar(currentTab = TopLevelTab.MESSAGES, onTabSelected = {}) }
        ) { padding ->
            MessagesScreen(
                conversations = previewConversations,
                onConversationClick = {},
                modifier = Modifier.padding(padding)
            )
        }
    }
}
