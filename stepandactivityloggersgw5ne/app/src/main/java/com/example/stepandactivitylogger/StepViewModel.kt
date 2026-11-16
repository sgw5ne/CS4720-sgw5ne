package com.example.stepandactivitylogger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stepandactivitylogger.data.DaySteps
import com.example.stepandactivitylogger.data.StepRepository
import com.example.stepandactivitylogger.sensors.StepMath
import com.example.stepandactivitylogger.sensors.StepMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class StepViewModel(private val repository: StepRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(StepUiState())
    val uiState: StateFlow<StepUiState> = _uiState.asStateFlow()

    val history: StateFlow<List<DaySteps>> = repository.observeHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private var today: DaySteps? = null
    private var loaded = false
    private var unsavedChanges = 0

    init {
        viewModelScope.launch {
            today = repository.getDay(repository.todayKey())
            loaded = true
            publishToday()
        }
    }

    fun onModeChanged(mode: StepMode, hasStepCounter: Boolean) {
        _uiState.value = _uiState.value.copy(mode = mode, hasStepCounter = hasStepCounter)
    }

    fun onPermissionResult(granted: Boolean, permanentlyDenied: Boolean) {
        _uiState.value = _uiState.value.copy(
            permissionPermanentlyDenied = !granted && permanentlyDenied
        )
    }

    fun onAccelerometer(x: Float, y: Float, z: Float, smoothed: Float) {
        _uiState.value = _uiState.value.copy(x = x, y = y, z = z, smoothed = smoothed)
    }

    fun onStepDetected() {
        if (!loaded) return
        rolloverIfNeeded()
        today = StepMath.applyDetectedStep(today, repository.todayKey())
        afterChange()
    }

    fun onCounterReading(total: Long) {
        if (!loaded) return
        rolloverIfNeeded()
        today = StepMath.applyCounterReading(today, repository.todayKey(), total)
        afterChange()
    }

    fun save() {
        val day = today ?: return
        unsavedChanges = 0
        viewModelScope.launch { repository.save(day) }
    }

    private fun afterChange() {
        publishToday()
        unsavedChanges++
        if (unsavedChanges >= SAVE_EVERY) save()
    }

    private fun publishToday() {
        _uiState.value = _uiState.value.copy(todaySteps = today?.steps ?: 0, todayDate = today?.date ?: repository.todayKey())
    }

    private fun rolloverIfNeeded() {
        val current = today
        if (current  != null && current.date != repository.todayKey()) {
            save()
            today = null
        }
    }

    companion object {
        private const val SAVE_EVERY = 10
        fun factory(repository: StepRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { StepViewModel(repository) }
        }
    }
}