package com.example.fool_tool.ui.presentation.ui_state

import androidx.compose.runtime.Immutable

@Immutable
data class PermissionsUiState(
    val notification: Boolean,
    val notificationChannel: Boolean,
    val alarm: Boolean,
) {
    val areAllGranted: Boolean
        get() = notification && notificationChannel && alarm
}