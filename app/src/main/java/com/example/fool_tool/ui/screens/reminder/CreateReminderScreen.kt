package com.example.fool_tool.ui.screens.reminder

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.fool_tool.R
import com.example.fool_tool.ui.components.shared.DateTimePicker

@Composable
fun CreateReminderScreen(
    onReminderCreated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateReminderViewModel = hiltViewModel(),
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current

    val uiState = viewModel.reminderUiState.collectAsState().value

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.messagesFlow.collect { message ->
                Toast.makeText(
                    context,
                    message.asString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }
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

        }

    }
}