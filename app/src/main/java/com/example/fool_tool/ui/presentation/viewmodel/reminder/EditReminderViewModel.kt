package com.example.fool_tool.ui.presentation.viewmodel.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fool_tool.core_android.alarm.AlarmScheduler
import com.example.fool_tool.core_android.notification.NotificationService
import com.example.fool_tool.domain.model.ReminderStatus
import com.example.fool_tool.domain.model.factory.ReminderFactory
import com.example.fool_tool.domain.repository.ReminderRepository
import com.example.fool_tool.domain.validation.ReminderValidator
import com.example.fool_tool.ui.presentation.ui_state.reminder.ReminderFormUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime


@HiltViewModel(assistedFactory = EditReminderViewModel.Factory::class)
class EditReminderViewModel @AssistedInject constructor(
    private val alarmScheduler: AlarmScheduler,
    private val reminderRepository: ReminderRepository,
    private val notificationService: NotificationService,
    private val validator: ReminderValidator,
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

    suspend fun attemptToUpdateReminder(): Boolean {
        val dateTimeError = validator.validateDateTime(_uiState.value.date)

        if (dateTimeError != null) {
            _uiState.update { it.copy(dateTimeError = dateTimeError) }
            return false
        }

        updateReminder()
        return true
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

    @AssistedFactory
    interface Factory {
        fun createViewModel(reminderId: Long): EditReminderViewModel
    }


}