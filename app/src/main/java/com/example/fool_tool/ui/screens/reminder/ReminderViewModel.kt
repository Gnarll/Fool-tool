package com.example.fool_tool.ui.screens.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fool_tool.data.repositories.ReminderRepository
import com.example.fool_tool.ui.UiState
import com.example.fool_tool.ui.model.Reminder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
) :
    ViewModel() {
    val reminders: StateFlow<UiState<List<Reminder>>> =
        reminderRepository.getAllRemindersStream()
            .map {
                UiState.Success(it) as UiState<List<Reminder>>
            }
            .catch { e ->
                emit(UiState.Failure(e))
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                initialValue = UiState.Loading
            )


}