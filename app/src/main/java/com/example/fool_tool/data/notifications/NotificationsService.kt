package com.example.fool_tool.data.notifications

interface NotificationsService {
    fun checkPrimaryPermissionIsGranted(): Boolean
    fun checkReminderChannelPermissionIsGranted(): Boolean
    fun openReminderChannelPermissionSettings(channelId: String)
    fun openPrimaryPermissionSettings()
}