package com.example.fool_tool.ui.presentation.validation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.fool_tool.R
import com.example.fool_tool.domain.validation.ValidationError

@Composable
fun ValidationError.asText(): String {
    return when (this) {
        is ValidationError.DateTimeValidationError -> stringResource(
            R.string.invalid_date_error
        )

        ValidationError.EmptyInputError -> stringResource(R.string.empty_input_error)
        is ValidationError.InputMaxSymbolsError -> stringResource(
            R.string.input_max_symbols_error,
            this.maxCount
        )
    }
}