package edu.virginia.cs4720.sgw5ne.bucketlist

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.virginia.cs4720.sgw5ne.bucketlist.ui.theme.BucketListTheme
import java.time.LocalDate
import kotlin.comparisons.compareBy

@Composable
fun ListScreen(vm: ListViewModel = viewModel()) {
    val context = LocalContext.current
    val sorted = vm.items().sortedWith(
        compareBy( { it.completed }, { it.dueDate })
    )
    LazyColumn {
        items(sorted) { item ->
            BucketRow(
                name = item.name,
                dueDate = item.dueDate,
                completed = item.completed,
                completedDate = item.completedDate,
                onToggle = { vm.toggle(item.id) },
                onEdit = {
                    val i = Intent(context, DetailActivity::class.java)
                    i.putExtra("ITEM_ID", item.id)
                    context.startActivity(i)
                }
            )
            HorizontalDivider()
        }
    }
}

@Composable
fun BucketRow(
    name: String,
    dueDate: LocalDate,
    completed: Boolean,
    completedDate: LocalDate?,
    onToggle: () -> Unit,
    onEdit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = completed, onCheckedChange = { onToggle() })
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(
                text = name
            )
            Text(
                text = "Due $dueDate"
            )
            completedDate?.let {
                Text(
                    text = "Completed $it"
                )
            }
        }
        TextButton(onClick = onEdit) { Text("Edit")}
    }
}

@Preview(showBackground = true)
@Composable
fun BucketRowPreview() {
    BucketListTheme {
        BucketRow(
            name = "Eat at Bodo's at 6 AM",
            dueDate = LocalDate.of(2026, 10, 1),
            completed = true,
            completedDate = LocalDate.of(2026, 9, 27),
            onToggle = {},
            onEdit = {}
        )
    }
}