package com.example.fool_tool.ui.presentation.viewmodel.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId


@HiltViewModel(assistedFactory = EditReminderViewModel.Factory::class)
class EditReminderViewModel @AssistedInject constructor(
    private val alarmScheduler: AlarmScheduler,
    private val reminderRepository: ReminderRepository,
    private val notificationService: NotificationService,
    private val validationConstants: ReminderValidationConstants,
    @Assisted private val reminderId: Long
) :
    ViewModel() {

    init {
        preloadReminder()
    }

    private val _isPreloaded = MutableStateFlow(false)
    val isPreloaded = _isPreloaded

    private val _uiState: MutableStateFlow<ReminderFormUiState> = MutableStateFlow(
        ReminderFormUiState(
            date = LocalDateTime.now(),
            title = "",
            description = ""
        )
    )

    val uiState: StateFlow<ReminderFormUiState> = _uiState


    fun onUiDateChanged(dateTime: LocalDateTime) {
        _uiState.update { state ->
            state.copy(date = dateTime)
        }
        validateDateTime(dateTime)
    }

    fun onUiTitleChanged(title: String) {
        _uiState.update { state ->
            state.copy(title = title)
        }
        validateTitle(title)
    }

    fun onUiDescriptionChanged(description: String) {
        _uiState.update { state ->
            state.copy(description = description)
        }
        validateDescription(description)
    }

    suspend fun attemptToUpdateReminder(): Boolean = when {
        validateDateTime(dateTime = _uiState.value.date) != null -> false
        validateTitle(title = _uiState.value.title) != null -> false
        validateDescription(description = _uiState.value.description) != null -> false
        else -> {
            updateReminder()
            true
        }

    }

    fun checkPermissions() =
        alarmScheduler.checkIsAlarmPermissionGranted() and
                notificationService.checkIsReminderChannelPermissionGranted() and
                notificationService.checkIsPrimaryPermissionGranted()


    fun preloadReminder() {
        viewModelScope.launch {
            val reminder = reminderRepository.getReminderById(reminderId)

            _uiState.update { state ->
                state.copy(
                    date = reminder.date,
                    title = reminder.title,
                    description = reminder.description
                )

            }

            _isPreloaded.value = true
        }
    }

    private suspend fun updateReminder() {
        val reminder = with(_uiState.value) {
            ReminderFactory.createReminder(
                id = reminderId,
                date = date,
                title = title,
                description = description,
                status = ReminderStatus.PENDING
            )
        }

        reminderRepository.updateReminder(reminder)
        alarmScheduler.schedule(reminder)
    }

    private fun validateDateTime(dateTime: LocalDateTime): ValidationError? {
        val validationError = when {
            LocalDateTime.now(ZoneId.systemDefault()) > dateTime -> DateTimeValidationError
            else -> null
        }
        _uiState.update { it.copy(dateTimeError = validationError) }
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
        _uiState.update { it.copy(titleError = validationError) }
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
        _uiState.update { it.copy(descriptionError = validationError) }
        return validationError
    }


    @AssistedFactory
    interface Factory {
        fun createViewModel(reminderId: Long): EditReminderViewModel
    }


}