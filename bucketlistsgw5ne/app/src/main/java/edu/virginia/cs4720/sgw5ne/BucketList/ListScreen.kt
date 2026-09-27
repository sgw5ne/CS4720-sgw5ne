package edu.virginia.cs4720.sgw5ne.BucketList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.virginia.cs4720.sgw5ne.BucketList.data.BucketItem
import java.time.LocalDate
import kotlin.comparisons.compareBy


@Composable
fun ListScreen(
    items: List<BucketItem>,
    onToggle: (BucketItem) -> Unit,
    onEdit: (BucketItem) -> Unit,
    onAdd: () -> Unit
) {
    val sorted = vm.items().sortedWith(
        compareBy({ it.completed }, { it.dueDate })
    )
    LazyColumn {
        items(sorted) { item ->
            BucketRow(
                name = item.name,
                dueDate = item.dueDate,
                done = item.completed,
                onToggle = { vm.toggle(item.id) },
                onEdit = { /* intent to DetailActivity with item.id */ }
            )
        }
    }
}

@Composable
fun BucketRow(item: BucketItem, onToggle: () -> Unit, onEdit: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = item.completed, onCheckedChange = { onToggle() })
        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, style = MaterialTheme.typography.titleMedium)
            Text("Due ${item.dueDate}")
        }
        IconButton(onClick = onEdit) {
            Icon(Icons.Default.Edit, contentDescription = "Edit ${item.name}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    ListScreen(
        items = listOf(
            BucketItem(name = "Bodo's at 6", dueDate = LocalDate.of(2026, 10, 1))
        ),
        onToggle = {}, onEdit = {}, onAdd = {}
    )
}