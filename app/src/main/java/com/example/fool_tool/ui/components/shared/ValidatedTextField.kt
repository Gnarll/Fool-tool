package com.example.fool_tool.ui.components.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fool_tool.core_android.util.validation.ValidationError

@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    error: ValidationError?,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
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
                    text = it.errorMessage.asString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        keyboardOptions = keyboardOptions,
        modifier = modifier
    )
}

@Preview
@Composable
fun ValidatedTextFieldPreview() {
    ValidatedTextField(
        value = "Placeholder",
        onValueChange = {},
        labelText = "Example",
        error = null,
        modifier = Modifier.fillMaxWidth()
    )
}