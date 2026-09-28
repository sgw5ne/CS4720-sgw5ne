package edu.virginia.cs4720.sgw5ne.bucketlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class DetailViewModel : ViewModel() {
    private var loadedId: String? = null

    var name by mutableStateOf("")
        private set
    var dueDate by mutableStateOf(LocalDate.now())
        private set
    var completed by mutableStateOf(false)
        private set
    var completedDate by mutableStateOf<LocalDate?>(null)
        private set
    var showDatePicker by mutableStateOf(false)
        private set

    fun start(id: String) {
        if (loadedId != null) return
        val item = BucketRepository.items.firstOrNull { it.id == id } ?: return
        loadedId = id
        name = item.name
        dueDate = item.dueDate
        completed = item.completed
        completedDate = item.completedDate
    }

    fun canSave() = name.isNotBlank()

    fun onNameChange(new:String) {name = new}
    fun openDatePicker() { showDatePicker = true }
    fun closeDatePicker() { showDatePicker = false }
    fun onDueDatePicked(date: LocalDate) {
        dueDate = date
        showDatePicker = false
    }

    fun onCompletedChange(done: Boolean) {
        completed = done
        completedDate = if (done) LocalDate.now() else null
    }

    fun save() {
        val id = loadedId ?: return
        if (name.isBlank()) return
        val original = BucketRepository.items.first { it.id == id }
        BucketRepository.update(
            original.copy(
                name = name,
                dueDate = dueDate,
                completed = completed,
                completedDate = completedDate
            )
        )
    }
}