package com.example.fool_tool.ui.presentation.ui_state.reminder

import com.example.fool_tool.domain.validation.ValidationError
import java.time.LocalDateTime

data class ReminderFormUiState(
    val date: LocalDateTime,
    val title: String,
    val description: String,
    val dateTimeError: ValidationError? = null,
    val titleError: ValidationError? = null,
    val descriptionError: ValidationError? = null,
)