package com.example.fool_tool.ui.screens.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.fool_tool.data.repositories.ReminderRepository
import com.example.fool_tool.utils.ReminderCreating
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
) :
    ViewModel() {
    val reminders = reminderRepository.getPagedReminders()
        .cachedIn(viewModelScope)


    fun tempInsert() {
        viewModelScope.launch {
            reminderRepository.createReminder(ReminderCreating.createReminder())
        }
    }

    fun deleteReminder(id: Long) {
        viewModelScope.launch {
            reminderRepository.deleteReminder(id)
        }
    }
}