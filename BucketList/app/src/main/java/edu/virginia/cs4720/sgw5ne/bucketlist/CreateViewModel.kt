package edu.virginia.cs4720.sgw5ne.bucketlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class CreateViewModel : ViewModel() {
    var name by mutableStateOf("")
        private set
    var dueDate by mutableStateOf<LocalDate?>(null)
        private set
    var showDatePicker by mutableStateOf(false)
        private set

    fun canSave() = name.isNotBlank() && dueDate != null

    fun onNameChange(new: String) { name = new }
    fun openDatePicker() { showDatePicker = true }
    fun closeDatePicker() { showDatePicker = false }
    fun onDueDatePicked() {
        dueDate = dueDate
        showDatePicker = false
    }

}