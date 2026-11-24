package edu.virginia.cs4720.sgw5ne.finalproject.data

/** What the UI needs to show a group. Will be filled from Firestore `groups` later. */
data class Group(
    val id: String,
    val name: String,
    val memberCount: Int,
    val isPublic: Boolean,
)
