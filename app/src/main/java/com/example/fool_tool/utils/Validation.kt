package com.example.fool_tool.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.fool_tool.R

class ErrorMessage(
    @get:StringRes
    val messageResId: Int,
    vararg val params: Any
) {
    @Composable
    fun asString(): String = stringResource(messageResId, *params)

    fun asString(context: Context): String = context.getString(messageResId, *params)
}

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