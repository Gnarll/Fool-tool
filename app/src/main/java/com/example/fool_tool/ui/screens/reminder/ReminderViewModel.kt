package com.example.fool_tool.ui.screens.reminder

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.fool_tool.data.alarm.AlarmScheduler
import com.example.fool_tool.data.repositories.ReminderRepository
import com.example.fool_tool.ui.model.Reminder
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val reminderRepository: ReminderRepository,
    private val alarmScheduler: AlarmScheduler
) :
    ViewModel() {


    private val _isPermissionGranted =
        MutableStateFlow(alarmScheduler.checkIsAlarmPermissionGranted())


    var _reminders: MutableStateFlow<ReminderUiState> = MutableStateFlow(
        ReminderUiState(
            isPermissionGranted = _isPermissionGranted.value,
            pagingData = reminderRepository.getPagedReminders().cachedIn(viewModelScope)
        )
    )
    val reminders: StateFlow<ReminderUiState> = _reminders.asStateFlow()

    init {
        observePermissionChanges()
    }

    fun deleteReminder(id: Long) {
        viewModelScope.launch {
            reminderRepository.deleteReminder(id)
        }
    }

    fun checkPermission() {
        _isPermissionGranted.value = alarmScheduler.checkIsAlarmPermissionGranted()
    }

    fun grantPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                addFlags(FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private fun observePermissionChanges() {
        viewModelScope.launch {
            _isPermissionGranted.collect { isGranted ->
                _reminders.value = _reminders.value.copy(isPermissionGranted = isGranted)
            }
        }
    }
}

data class ReminderUiState(
    val isPermissionGranted: Boolean,
    val pagingData: Flow<PagingData<Reminder>>
)