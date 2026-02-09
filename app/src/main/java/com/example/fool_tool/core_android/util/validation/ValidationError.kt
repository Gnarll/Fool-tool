package com.example.fool_tool.core_android.util.validation

import com.example.fool_tool.R

interface ValidationError {
    val errorMessage: ErrorMessage
}

object EmptyInputError : ValidationError {
    override val errorMessage: ErrorMessage
        get() = ErrorMessage(R.string.empty_input_error)
}

object DateTimeValidationError : ValidationError {
    override val errorMessage: ErrorMessage
        get() = ErrorMessage(R.string.invalid_date_error)
}

class InputMaxSymbolsError(val maxCount: Int) : ValidationError {
    override val errorMessage: ErrorMessage
        get() = ErrorMessage(R.string.input_max_symbols_error, maxCount)
}