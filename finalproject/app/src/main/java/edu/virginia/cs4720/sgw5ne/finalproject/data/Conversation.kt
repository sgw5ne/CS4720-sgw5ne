package edu.virginia.cs4720.sgw5ne.finalproject.data

/**
 * One row in the Messages list: a group chat or a DM, with its latest message.
 * Will be filled from Firestore `groups/{groupId}/messages` later.
 */
data class Conversation(
    val id: String,
    /** Group name, or "@username" for a direct message. */
    val name: String,
    val lastMessage: String,
    /** Display string, e.g. "15:20" or "Yesterday". */
    val lastMessageTime: String,
    val unreadCount: Int = 0,
)
