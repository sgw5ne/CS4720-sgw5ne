package edu.virginia.cs4720.sgw5ne.bucketlist

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.comparisons.compareBy

@Composable
fun ListScreen(vm: ListViewModel = viewModel()) {
    val sorted = vm.items().sortedWith(
        compareBy( { it.completed }, { it.dueDate })
    )
    LazyColumn {
        items(sorted) { item ->
            BucketRow(
                name = item.name,
                dueDate = item.dueDate,
                completed = item.completed,
                onToggle = { vm.toggle(item.id) },
                onEdit = { /* intent to DetailActivity with item.id */ }
            )
        }
    }
}