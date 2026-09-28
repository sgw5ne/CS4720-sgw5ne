package edu.virginia.cs4720.sgw5ne.bucketlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DetailScreen(id: String, onDone: () -> Unit, vm: DetailViewModel = viewModel()) {
    vm.start(id)

    Column(modifier = Modifier.padding(24.dp)) {
        Text("Edit Item", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = vm.name,
            onValueChange = { vm.onNameChange(it) },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { vm.openDatePicker() }) {
            Text("Due ${vm.dueDate.pretty()}")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = vm.completed,
                onCheckedChange = { vm.onCompletedChange(it) }
            )
            Text("Completed")
        }
        Text(vm.completedDate?.let { "Completed on $it" } ?: "Not completed yet")
        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Button(
                onClick = onDone,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Cancel")
            }
            Button(
                onClick = {vm.save(); onDone() },
                enabled = vm.canSave()
            ) {
                Text("Save")
            }
        }

        if (vm.showDatePicker) {
            val pickerState = rememberDatePickerState(
                initialSelectedDateMillis = vm.dueDate.toEpochMillis()
            )
            DatePickerDialog(
                onDismissRequest = { vm.closeDatePicker() },
                confirmButton = {
                    Button(onClick = {
                        val millis = pickerState.selectedDateMillis
                        if (millis != null) vm.onDueDatePicked(millis.toLocalDate())
                        else vm.closeDatePicker()
                    }) {Text("OK")}
                },
                dismissButton = {
                    Button(onClick = { vm.closeDatePicker() }) {Text("Cancel")}
                }
            ) {
                DatePicker(state = pickerState)
            }
        }
    }
}