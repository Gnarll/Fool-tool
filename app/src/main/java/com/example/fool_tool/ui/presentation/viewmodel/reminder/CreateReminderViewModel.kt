package com.example.fool_tool.ui.presentation.viewmodel.reminder

import androidx.lifecycle.ViewModel
import com.example.fool_tool.core_android.alarm.AlarmScheduler
import com.example.fool_tool.core_android.notification.NotificationService
import com.example.fool_tool.core_android.util.validation.DateTimeValidationError
import com.example.fool_tool.core_android.util.validation.EmptyInputError
import com.example.fool_tool.core_android.util.validation.InputMaxSymbolsError
import com.example.fool_tool.core_android.util.validation.ValidationError
import com.example.fool_tool.di.ReminderValidationConstants
import com.example.fool_tool.domain.model.ReminderStatus
import com.example.fool_tool.domain.model.factory.ReminderFactory
import com.example.fool_tool.domain.repository.ReminderRepository
import com.example.fool_tool.ui.components.reminder.ReminderFormUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject


@HiltViewModel
class CreateReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val alarmScheduler: AlarmScheduler,
    private val notificationService: NotificationService,
    private val validationConstants: ReminderValidationConstants,
) : ViewModel() {

    private var _reminderFormUiState = MutableStateFlow(
        ReminderFormUiState(
            date = LocalDateTime.now(), title = "", description = "",
        )
    )
    val reminderFormUiState: StateFlow<ReminderFormUiState> =
        _reminderFormUiState.asStateFlow()


    fun onUiDateChanged(dateTime: LocalDateTime) {
        _reminderFormUiState.update {
            it.copy(
                date = dateTime,
            )
        }
        validateDateTime(dateTime)
    }

    fun onUiTitleChanged(title: String) {
        _reminderFormUiState.update {
            it.copy(
                title = title,
            )
        }
        validateTitle(title)
    }

    fun onUiDescriptionChanged(description: String) {
        _reminderFormUiState.update {
            it.copy(
                description = description,
            )
        }
        validateDescription(description)
    }

    suspend fun attemptToCreateReminder(): Boolean = when {
        validateDateTime(dateTime = _reminderFormUiState.value.date) != null -> false
        validateTitle(title = _reminderFormUiState.value.title) != null -> false
        validateDescription(description = _reminderFormUiState.value.description) != null -> false
        else -> {
            createReminder()
            true
        }

    }

    fun checkPermissions() = alarmScheduler.checkIsAlarmPermissionGranted() and
            notificationService.checkIsReminderChannelPermissionGranted() and
            notificationService.checkIsPrimaryPermissionGranted()

    private fun validateDateTime(dateTime: LocalDateTime): ValidationError? {
        val validationError = when {
            LocalDateTime.now(ZoneId.systemDefault()) > dateTime -> DateTimeValidationError
            else -> null
        }
        _reminderFormUiState.update { it.copy(dateTimeError = validationError) }
        return validationError
    }

    private fun validateTitle(title: String): ValidationError? {
        val validationError = when {
            title.isEmpty() -> EmptyInputError
            title.length > validationConstants.titleMaxSymbols -> InputMaxSymbolsError(
                validationConstants.titleMaxSymbols
            )

            else -> null
        }
        _reminderFormUiState.update { it.copy(titleError = validationError) }
        return validationError
    }

    private fun validateDescription(description: String): ValidationError? {
        val validationError = when {
            description.isEmpty() -> EmptyInputError
            description.length > validationConstants.descriptionMaxSymbols -> InputMaxSymbolsError(
                validationConstants.descriptionMaxSymbols
            )

            else -> null
        }
        _reminderFormUiState.update { it.copy(descriptionError = validationError) }
        return validationError
    }

    private suspend fun createReminder() {
        val reminder = with(reminderFormUiState.value) {
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