package com.example.fool_tool.domain.validation

sealed interface ValidationError {
    data object EmptyInputError : ValidationError
    data object DateTimeValidationError : ValidationError
    data class InputMaxSymbolsError(val maxCount: Int) : ValidationError
}