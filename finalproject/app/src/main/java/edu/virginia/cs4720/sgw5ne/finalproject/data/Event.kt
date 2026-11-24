package edu.virginia.cs4720.sgw5ne.finalproject.data

/**
 * What the UI needs to show an event. Will be filled from the Firestore `events`
 * collection later; date/time are display strings for now.
 */
data class Event(
    val id: String,
    val title: String,
    val groupName: String,
    val location: String,
    val date: String,
    val time: String,
    val endTime: String = "",
    /** e.g. "Public" or "Private - approval required". */
    val visibility: String = "",
    /** True while the current user's request to join is waiting on the organizer. */
    val isPendingApproval: Boolean = false,
)
