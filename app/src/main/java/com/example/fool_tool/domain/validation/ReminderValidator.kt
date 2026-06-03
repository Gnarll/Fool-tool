package com.example.fool_tool.domain.validation

import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class ReminderValidator @Inject constructor() {

    fun validateTitle(title: String): ValidationError? {
        return when {
            title.isEmpty() -> ValidationError.EmptyInputError
            title.length > TITLE_MAX_SYMBOLS -> ValidationError.InputMaxSymbolsError(
                TITLE_MAX_SYMBOLS
            )

            else -> null
        }
    }

    fun validateDateTime(dateTime: LocalDateTime): ValidationError? {
        return when {
            LocalDateTime.now(ZoneId.systemDefault()) > dateTime -> ValidationError.DateTimeValidationError
            else -> null
        }
    }

    fun validateDescription(description: String): ValidationError? {
        return when {
            description.length > DESCRIPTION_MAX_SYMBOLS -> ValidationError.InputMaxSymbolsError(
                DESCRIPTION_MAX_SYMBOLS
            )

            else -> null
        }
    }

    companion object {
        private const val TITLE_MAX_SYMBOLS = 35
        private const val DESCRIPTION_MAX_SYMBOLS = 80
    }
}