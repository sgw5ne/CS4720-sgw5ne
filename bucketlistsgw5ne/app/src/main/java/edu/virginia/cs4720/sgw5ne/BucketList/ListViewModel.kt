package edu.virginia.cs4720.sgw5ne.BucketList

import androidx.lifecycle.ViewModel
import edu.virginia.cs4720.sgw5ne.BucketList.data.BucketItem
import edu.virginia.cs4720.sgw5ne.BucketList.data.BucketRepository
import java.time.LocalDate

class ListViewModel : ViewModel() {
    val items: List<BucketItem>
        get() = BucketRepository.items.sortedWith(
            compareBy({it.completed}, {it.dueDate})
        )

    fun toggle(item: BucketItem) {
        val nowCompleted: !item.completed
        BucketRepository.update(
            item.copy(
                completed = nowCompleted,
                completedDate = if (nowCompleted) LocalDate.now() else null
            )
        )
    }
}