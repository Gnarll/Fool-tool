package com.example.fool_tool.ui.components.reminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.fool_tool.R
import com.example.fool_tool.ui.model.Reminder

@Composable
fun RemindersList(reminders: LazyPagingItems<Reminder>, modifier: Modifier = Modifier) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        modifier = modifier.fillMaxSize()
    ) {
        items(
            count = reminders.itemCount,
            key = { index -> reminders[index]?.id ?: index }) { index ->
            val reminder = reminders[index]
            reminder?.let {
                ReminderItem(reminder = reminder)
            }
        }
        if (reminders.loadState.append is LoadState.Loading) {
            item {
                CircularProgressIndicator()
            }
        }
    }

}
