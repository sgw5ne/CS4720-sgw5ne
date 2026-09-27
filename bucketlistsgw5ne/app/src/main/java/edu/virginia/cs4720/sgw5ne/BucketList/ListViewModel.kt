package edu.virginia.cs4720.sgw5ne.BucketList

import androidx.lifecycle.ViewModel
import edu.virginia.cs4720.sgw5ne.BucketList.data.BucketItem
import edu.virginia.cs4720.sgw5ne.BucketList.data.BucketRepository
import java.time.LocalDate

class ListViewModel : ViewModel() {
    fun items() = BucketRepository.items
    fun toggle(id: String) {
        val item = BucketRepository.items.first { it.id == id }
        BucketRepository.update(
            if (item.completed) item.copy(completed = false, completedDate = null)
            else item.copy(completed = true, completedDate = LocalDate.now())
        )
    }
}