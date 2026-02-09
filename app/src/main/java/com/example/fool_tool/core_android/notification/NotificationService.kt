package com.example.fool_tool.core_android.notification

import com.example.fool_tool.domain.model.Reminder

interface NotificationService {

    fun sendReminderNotification(reminder: Reminder)
    fun checkIsPrimaryPermissionGranted(): Boolean
    fun checkIsReminderChannelPermissionGranted(): Boolean
    fun openReminderChannelPermissionSettings()
    fun openPrimaryPermissionSettings()
}