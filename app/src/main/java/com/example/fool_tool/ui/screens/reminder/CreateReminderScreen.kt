package com.example.fool_tool.ui.screens.reminder

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.fool_tool.R
import com.example.fool_tool.ui.components.shared.DateTimePicker
import com.example.fool_tool.ui.components.shared.ValidatedTextField
import kotlinx.coroutines.launch

@Composable
fun CreateReminderScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateReminderViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val uiState = viewModel.createReminderUiState.collectAsState().value

    val isFormValid = uiState.titleError == null
            && uiState.dateTimeError == null
            && uiState.descriptionError == null

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val isGranted = viewModel.checkPermission()
                if (!isGranted) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.permission_alarms_error),
                        Toast.LENGTH_SHORT
                    ).show()
                    onNavigateBack()
                }
            }
        }
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_x_x_large)),
        contentAlignment = Alignment.Center
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))) {
            DateTimePicker(
                dateTime = uiState.date,
                onDateTimeChanged = viewModel::onUiDateChanged,
                error = uiState.dateTimeError,
            )
            ValidatedTextField(
                value = uiState.title,
                onValueChange = viewModel::onUiTitleChanged,
                labelText = stringResource(R.string.title),
                error = uiState.titleError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()

            )
            ValidatedTextField(
                value = uiState.description,
                onValueChange = viewModel::onUiDescriptionChanged,
                labelText = stringResource(R.string.description),
                error = uiState.descriptionError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                minLines = 2,
                maxLines = Int.MAX_VALUE,
                modifier = Modifier.fillMaxWidth()

            )
            Button(
                onClick = {
                    coroutineScope.launch {
                        val isSucceed = viewModel.attemptToCreateReminder()
                        if (isSucceed) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.create_reminder_success),
                                Toast.LENGTH_SHORT
                            ).show()
                            onNavigateBack()
                        }
                    }
                },
                enabled = isFormValid,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_medium))
            ) {
                Text(text = stringResource(R.string.create_reminder))
            }
        }
    }
}