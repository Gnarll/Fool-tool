package com.example.fool_tool.ui.screens.reminder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fool_tool.R
import com.example.fool_tool.ui.UiState

@Composable
fun ReminderScreen(
    onCreateReminder: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReminderViewModel = hiltViewModel()
) {
    val reminderItems =
        viewModel.reminders.collectAsStateWithLifecycle().value

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when (reminderItems) {
            is UiState.Success -> {
                if (reminderItems.value.isEmpty()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                }

            }

            is UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Failure -> {
                Text(
                    text = stringResource(R.string.error_msg_something_went_wrong),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}