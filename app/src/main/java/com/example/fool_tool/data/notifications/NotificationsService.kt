package com.example.fool_tool.data.notifications

import com.example.fool_tool.ui.model.Reminder

interface NotificationsService {

    fun sendReminderNotification(reminder: Reminder)
    fun checkPrimaryPermissionIsGranted(): Boolean
    fun checkReminderChannelPermissionIsGranted(): Boolean
    fun openReminderChannelPermissionSettings(channelId: String)
    fun openPrimaryPermissionSettings()
}