package com.example.fool_tool.ui.screens.reminder

import androidx.lifecycle.ViewModel
import com.example.fool_tool.data.alarm.AlarmScheduler
import com.example.fool_tool.data.repositories.ReminderRepository
import com.example.fool_tool.ui.model.ReminderStatus
import com.example.fool_tool.utils.DateTimeValidationError
import com.example.fool_tool.utils.EmptyInputError
import com.example.fool_tool.utils.InputMaxSymbolsError
import com.example.fool_tool.utils.ReminderCreating
import com.example.fool_tool.utils.ValidationError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject


data class CreateReminderUiState(
    val date: LocalDateTime,
    val title: String,
    val description: String,
    val dateTimeError: ValidationError? = null,
    val titleError: ValidationError? = null,
    val descriptionError: ValidationError? = null
)

@HiltViewModel
class CreateReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    private var _createReminderUiState = MutableStateFlow(
        CreateReminderUiState(
            date = LocalDateTime.now(), title = "", description = "",
        )
    )
    val createReminderUiState: StateFlow<CreateReminderUiState> =
        _createReminderUiState.asStateFlow()


    fun onUiDateChanged(dateTime: LocalDateTime) {
        _createReminderUiState.update {
            it.copy(
                date = dateTime,
            )
        }
        validateDateTime(dateTime)
    }

    fun onUiTitleChanged(title: String) {
        _createReminderUiState.update {
            it.copy(
                title = title,
            )
        }
        validateTitle(title)
    }

    fun onUiDescriptionChanged(description: String) {
        _createReminderUiState.update {
            it.copy(
                description = description,
            )
        }
        validateDescription(description)
    }

    suspend fun attemptToCreateReminder(): Boolean = when {
        validateDateTime(dateTime = _createReminderUiState.value.date) != null -> false
        validateTitle(title = _createReminderUiState.value.title) != null -> false
        validateDescription(description = _createReminderUiState.value.description) != null -> false
        else -> {
            createReminder()
            true
        }

    }

    fun checkPermission() = alarmScheduler.checkIsAlarmPermissionGranted()

    private fun validateDateTime(dateTime: LocalDateTime): ValidationError? {
        val validationError = when {
            LocalDateTime.now(ZoneId.systemDefault()) > dateTime -> DateTimeValidationError
            else -> null
        }
        _createReminderUiState.update { it.copy(dateTimeError = validationError) }
        return validationError
    }

    private fun validateTitle(title: String): ValidationError? {
        val validationError = when {
            title.isEmpty() -> EmptyInputError
            title.length > TITLE_MAX_SYMBOLS -> InputMaxSymbolsError(TITLE_MAX_SYMBOLS)
            else -> null
        }
        _createReminderUiState.update { it.copy(titleError = validationError) }
        return validationError
    }

    private fun validateDescription(description: String): ValidationError? {
        val validationError = when {
            description.isEmpty() -> EmptyInputError
            description.length > DESCRIPTION_MAX_SYMBOLS -> InputMaxSymbolsError(
                DESCRIPTION_MAX_SYMBOLS
            )

            else -> null
        }
        _createReminderUiState.update { it.copy(descriptionError = validationError) }
        return validationError
    }

    private suspend fun createReminder() {
        val reminder = with(createReminderUiState.value) {
            ReminderCreating.createReminder(
                date = date,
                title = title,
                description = description,
                status = ReminderStatus.PENDING
            )
        }

        reminderRepository.createReminder(reminder)
        alarmScheduler.schedule(reminder)
    }

    private companion object {
        const val TITLE_MAX_SYMBOLS = 35
        const val DESCRIPTION_MAX_SYMBOLS = 80
    }
}