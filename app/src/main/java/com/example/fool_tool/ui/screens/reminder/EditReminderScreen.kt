package com.example.fool_tool.ui.screens.reminder

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.fool_tool.R
import com.example.fool_tool.ui.components.reminder.ReminderForm
import kotlinx.coroutines.launch

@Composable
fun EditReminderScreen(
    reminderId: Long,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditReminderViewModel = hiltViewModel<EditReminderViewModel, EditReminderViewModel.Factory> { factory ->
        factory.createViewModel(reminderId = reminderId)
    },
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val coroutineScope = rememberCoroutineScope()

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

    val uiState = viewModel.uiState.collectAsState()
    val isPreloaded by viewModel.isPreloaded.collectAsState()

    if (isPreloaded) {
        Box(
            modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_x_x_large)),
            contentAlignment = Alignment.Center
        ) {
            ReminderForm(
                uiStateProvider = { uiState.value },
                onUiDateChanged = viewModel::onUiDateChanged,
                onUiTitleChanged = viewModel::onUiTitleChanged,
                onUiDescriptionChanged = viewModel::onUiDescriptionChanged,
                onConfirm = {
                    coroutineScope.launch {
                        val isSucceed = viewModel.attemptToUpdateReminder()
                        if (isSucceed) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.update_reminder_success),
                                Toast.LENGTH_SHORT
                            ).show()
                            onNavigateBack()
                        }
                    }
                },
            )
        }
    } else {
        CircularProgressIndicator()
    }
}