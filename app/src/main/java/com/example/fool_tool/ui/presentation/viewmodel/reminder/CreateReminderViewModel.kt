package com.example.fool_tool.ui.presentation.viewmodel.reminder

import androidx.lifecycle.ViewModel
import com.example.fool_tool.core_android.alarm.AlarmScheduler
import com.example.fool_tool.core_android.notification.NotificationService
import com.example.fool_tool.domain.model.ReminderStatus
import com.example.fool_tool.domain.model.factory.ReminderFactory
import com.example.fool_tool.domain.repository.ReminderRepository
import com.example.fool_tool.domain.validation.ReminderValidator
import com.example.fool_tool.domain.validation.ValidationError
import com.example.fool_tool.ui.presentation.ui_state.reminder.ReminderFormUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import javax.inject.Inject


@HiltViewModel
class CreateReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val alarmScheduler: AlarmScheduler,
    private val notificationService: NotificationService,
    private val validator: ReminderValidator,
) : ViewModel() {

    private var _uiState = MutableStateFlow(
        ReminderFormUiState(
            date = LocalDateTime.now(),
            title = "",
            description = "",
            titleError = ValidationError.EmptyInputError
        )
    )
    val uiState: StateFlow<ReminderFormUiState> =
        _uiState.asStateFlow()


    fun onUiDateChanged(dateTime: LocalDateTime) {
        val error = validator.validateDateTime(dateTime)
        _uiState.update { state ->
            state.copy(date = dateTime, dateTimeError = error)
        }
    }

    fun onUiTitleChanged(title: String) {
        val error = validator.validateTitle(title)
        _uiState.update { state ->
            state.copy(title = title, titleError = error)
        }
    }

    fun onUiDescriptionChanged(description: String) {
        val error = validator.validateDescription(description)
        _uiState.update { state ->
            state.copy(description = description, descriptionError = error)
        }
    }

    suspend fun attemptToCreateReminder(): Boolean {
        val dateTimeError = validator.validateDateTime(_uiState.value.date)

        if (dateTimeError != null) {
            _uiState.update { it.copy(dateTimeError = dateTimeError) }
            return false
        }

        createReminder()
        return true
    }

    fun checkPermissions() = alarmScheduler.checkIsAlarmPermissionGranted() and
            notificationService.checkIsReminderChannelPermissionGranted() and
            notificationService.checkIsPrimaryPermissionGranted()


    private suspend fun createReminder() {
        val reminder = with(uiState.value) {
            ReminderFactory.createReminder(
                date = date,
                title = title,
                description = description,
                status = ReminderStatus.PENDING
            )
        }

        reminderRepository.insertReminder(reminder)
        alarmScheduler.schedule(reminder)
    }
}