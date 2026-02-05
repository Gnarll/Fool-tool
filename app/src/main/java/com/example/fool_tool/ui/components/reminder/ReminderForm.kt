package com.example.fool_tool.ui.components.reminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.example.fool_tool.R
import com.example.fool_tool.ui.components.shared.DateTimePicker
import com.example.fool_tool.ui.components.shared.ValidatedTextField
import com.example.fool_tool.utils.ValidationError
import java.time.LocalDateTime

data class ReminderFormUiState(
    val date: LocalDateTime,
    val title: String,
    val description: String,
    val dateTimeError: ValidationError? = null,
    val titleError: ValidationError? = null,
    val descriptionError: ValidationError? = null,
)

@Composable
fun ReminderForm(
    uiStateProvider: () -> ReminderFormUiState,
    onUiDateChanged: (dateTime: LocalDateTime) -> Unit,
    onUiTitleChanged: (title: String) -> Unit,
    onUiDescriptionChanged: (description: String) -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiStateValue = uiStateProvider()

    val isFormValid = uiStateValue.titleError == null
            && uiStateValue.dateTimeError == null
            && uiStateValue.descriptionError == null

    Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))) {
        DateTimePicker(
            dateTime = uiStateValue.date,
            onDateTimeChanged = onUiDateChanged,
            error = uiStateValue.dateTimeError,
        )
        ValidatedTextField(
            value = uiStateValue.title,
            onValueChange = onUiTitleChanged,
            labelText = stringResource(R.string.title),
            error = uiStateValue.titleError,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()

        )
        ValidatedTextField(
            value = uiStateValue.description,
            onValueChange = onUiDescriptionChanged,
            labelText = stringResource(R.string.description),
            error = uiStateValue.descriptionError,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            minLines = 2,
            maxLines = Int.MAX_VALUE,
            modifier = Modifier.fillMaxWidth()

        )
        Button(
            onClick = onConfirm,
            enabled = isFormValid,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_medium))
        ) {
            Text(text = stringResource(R.string.confirm_btn_text))
        }
    }
}