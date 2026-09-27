package edu.virginia.cs4720.sgw5ne.bucketlist

import androidx.compose.runtime.mutableStateOf
import kotlin.collections.plus

object BucketRepository {
     var items by mutableStateOf(listOf<BucketItem>())
            private set

    fun add(item: BucketItem) { items = items + item }

    fun update(updated: BucketItem) {
        items = items.map { if (it.id == updated.id) updated else it }
    }
}