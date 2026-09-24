package edu.virginia.cs4720.sgw5ne.BucketList.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.time.LocalDate

object BucketRepository {
     var items by mutableStateOf(
         listOf(
             BucketItem(name = "Eat at Bodo's at 6 AM", dueDate = LocalDate.now().plusDays(7))
         )
     )
            private set

    fun get(id: String): BucketItem? = items.find {it.id == id}

    fun add(item: BucketItem) {
        items = items + item
    }

    fun update(item: BucketItem) {
        items = items.map { if (it.id == item.id) item else it }
    }
}