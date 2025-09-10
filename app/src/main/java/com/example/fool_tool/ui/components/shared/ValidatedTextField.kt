package com.example.fool_tool.ui.components.shared

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.fool_tool.utils.ValidationError

@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    error: ValidationError?,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.bodyLarge,
        label = {
            Text(
                text = labelText,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        supportingText = {
            error?.let {
                Text(
                    text = stringResource(it.messageResId),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        keyboardOptions = keyboardOptions,
        modifier = modifier
    )
}