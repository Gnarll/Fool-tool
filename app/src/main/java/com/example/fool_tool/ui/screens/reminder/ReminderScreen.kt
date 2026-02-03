package com.example.fool_tool.ui.screens.reminder

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.fool_tool.R
import com.example.fool_tool.data.alarm.ScheduleResult
import com.example.fool_tool.ui.components.reminder.RemindersPagedList
import com.example.fool_tool.ui.model.Reminder
import kotlinx.coroutines.launch

@Composable
fun ReminderScreen(
    onCreateReminder: () -> Unit,
    onEditReminder: (id: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReminderViewModel = hiltViewModel()
) {

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current

    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.checkPermission()
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    val remindersUiState =
        viewModel.reminders.collectAsState().value

    val coroutineScope = rememberCoroutineScope()

    var editDialogParams by rememberSaveable {
        mutableStateOf(
            Pair<Boolean, Reminder?>(
                false,
                null
            )
        )
    }
    val onDismissDialog = { editDialogParams = editDialogParams.copy(first = false) }
    val onEditReminderItem: (Reminder) -> Unit = { reminder ->
        editDialogParams = editDialogParams.copy(first = true, second = reminder)
    }
    val onConfirmDialog = {
        editDialogParams = editDialogParams.copy(first = false)
        editDialogParams.second?.id?.let {
            onEditReminder(it)
        }
        Unit
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(top = dimensionResource(R.dimen.padding_medium))
    ) {


        if (editDialogParams.first) {
            AlertDialog(
                onDismissRequest = onDismissDialog,
                text = {
                    Text(
                        text = stringResource(R.string.ensure_edit_reminder_question),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                confirmButton = {
                    Button(onClick = onConfirmDialog) {
                        Text(
                            text = stringResource(R.string.positive_answer),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                },
                dismissButton = {
                    Button(onClick = onDismissDialog) {
                        Text(
                            text = stringResource(R.string.negative_answer),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                },
                modifier = modifier
            )
        }

        if (remindersUiState.isPermissionGranted) {

            val reminderItems = remindersUiState.pagingData.collectAsLazyPagingItems()

            when (reminderItems.loadState.refresh) {
                is LoadState.Error -> Text(
                    text = stringResource(R.string.error_msg_something_went_wrong),
                    color = MaterialTheme.colorScheme.error,
                )

                LoadState.Loading -> CircularProgressIndicator()

                else -> {
                    if (reminderItems.itemCount == 0) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = stringResource(R.string.no_reminders_info),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
                            Button(onClick = onCreateReminder) {
                                Text(text = stringResource(R.string.create))
                            }
                        }
                    } else {
                        RemindersPagedList(
                            reminders = reminderItems,
                            onDeleteReminder = { id ->
                                coroutineScope.launch { viewModel.deleteReminder(id) }
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.reminder_deleted),
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onCancelReminder = { reminder ->
                                coroutineScope.launch {
                                    viewModel.onCancelReminder(reminder)
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.reminder_cancelled),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            onActivateReminder = { reminder ->
                                coroutineScope.launch {
                                    val scheduleResult: ScheduleResult =
                                        viewModel.onActivateReminder(reminder)

                                    val toastText = when (scheduleResult) {
                                        ScheduleResult.FailedWithInvalidTime ->
                                            context.getString(R.string.reminder_impossible_activate_invalid_date)

                                        ScheduleResult.FailedWithNoPermission ->
                                            context.getString(R.string.permission_alarms_error)

                                        ScheduleResult.Success ->
                                            context.getString(R.string.reminder_activated)
                                    }

                                    Toast.makeText(
                                        context,
                                        toastText,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            onEditReminder = onEditReminderItem,
                            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
                        )
                    }
                }
            }

            if (reminderItems.loadState.refresh is LoadState.NotLoading && reminderItems.itemCount > 0)
                FloatingActionButton(
                    onClick = onCreateReminder,
                    content = {
                        Icon(
                            painter = painterResource(R.drawable.ic_plus),
                            contentDescription = stringResource(R.string.add_reminder)
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(dimensionResource(R.dimen.padding_x_x_large))
                )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
            ) {

                Text(
                    text = stringResource(R.string.permission_alarms_warning),
                    textAlign = TextAlign.Center
                )
                Button(onClick = viewModel::grantPermission) {
                    Text(text = stringResource(R.string.to_permission_settings))
                }
            }
        }
    }
}