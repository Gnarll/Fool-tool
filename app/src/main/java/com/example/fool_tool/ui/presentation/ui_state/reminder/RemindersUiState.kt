package com.example.fool_tool.ui.presentation.ui_state.reminder

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import com.example.fool_tool.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

@Immutable
data class RemindersUiState(
    val isPreloading: Boolean,
    val indexToScrollTo: Int?,
    val pagingRemindersFlow: Flow<PagingData<Reminder>>
)