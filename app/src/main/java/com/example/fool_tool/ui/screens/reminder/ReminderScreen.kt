package com.example.fool_tool.ui.screens.reminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.fool_tool.R
import com.example.fool_tool.ui.components.reminder.ReminderItem

@Composable
fun ReminderScreen(
    onCreateReminder: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReminderViewModel = hiltViewModel()
) {
    val reminderItems =
        viewModel.reminders.collectAsLazyPagingItems()

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        when (reminderItems.loadState.refresh) {
            is LoadState.Error -> Text(
                text = stringResource(R.string.error_msg_something_went_wrong),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )

            LoadState.Loading -> CircularProgressIndicator(Modifier.align(alignment = Alignment.Center))

            else -> {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (reminderItems.itemCount == 0) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.align(
                                        Alignment.Center
                                    )
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
                            }
                        }
                    } else {
                        items(
                            count = reminderItems.itemCount,
                            key = { index -> reminderItems[index]?.id ?: index }) { index ->
                            val reminder = reminderItems[index]
                            reminder?.let {
                                ReminderItem(reminder = reminder)
                            }
                        }
                        if (reminderItems.loadState.append is LoadState.Loading) {
                            item {
                                CircularProgressIndicator()
                            }
                        }
                    }

                }
            }
        }

        if (reminderItems.loadState.refresh is LoadState.NotLoading && reminderItems.itemCount > 0)
            FloatingActionButton(
                onClick = {
                    viewModel.tempInsert()
//                    onCreateReminder
                },
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