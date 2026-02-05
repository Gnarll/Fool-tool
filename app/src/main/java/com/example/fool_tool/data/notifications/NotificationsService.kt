package com.example.fool_tool.data.notifications

import com.example.fool_tool.ui.model.Reminder

interface NotificationsService {

    fun sendReminderNotification(reminder: Reminder)
    fun checkIsPrimaryPermissionGranted(): Boolean
    fun checkIsReminderChannelPermissionGranted(): Boolean
    fun openReminderChannelPermissionSettings()
    fun openPrimaryPermissionSettings()
}