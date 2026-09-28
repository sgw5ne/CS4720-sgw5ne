package edu.virginia.cs4720.sgw5ne.bucketlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CreateScreen(onDone:() -> Unit, vm: CreateViewModel = viewModel()) {
    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = vm.name,
                onValueChange = { vm.onNameChange(it) },
                label = { Text("What do you want to do?") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { vm.openDatePicker() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(vm.dueDate?.let { "Due $it" } ?: "Pick a due date!")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = onDone, modifier = Modifier.weight(1f)) {
                    Text("Cancel")
                }
                Button(
                    onClick = { vm.save(); onDone() },
                    enabled = vm.canSave(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save")
                }
            }
        }
    }

    if (vm.showDatePicker) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = vm.dueDate?.toEpochMillis()
        )
        DatePickerDialog(
            onDismissRequest = { vm.closeDatePicker() },
            confirmButton = {
                Button(onClick = {
                    val millis = pickerState.selectedDateMillis
                    if (millis != null) vm.onDueDatePicked(millis.toLocalDate())
                    else vm.closeDatePicker()
                }) { Text("OK") }
            },
            dismissButton = {
                Button(onClick = { vm.closeDatePicker() }) { Text("Cancel") }
            }

        ) {
            DatePicker(state = pickerState)
        }
    }
}
