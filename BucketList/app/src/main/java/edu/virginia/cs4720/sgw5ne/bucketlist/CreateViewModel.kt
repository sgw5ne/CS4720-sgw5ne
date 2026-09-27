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


}