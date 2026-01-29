package com.example.fool_tool.data.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.example.fool_tool.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationsServiceImpl @Inject constructor(@ApplicationContext private val context: Context) :
    NotificationsService {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    init {
        recreateReminderNotificationsChannel()
    }

    override fun checkPrimaryPermissionIsGranted(): Boolean =
        notificationManager.areNotificationsEnabled()

    override fun checkReminderChannelPermissionIsGranted(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return true

        val reminderChannel = notificationManager.getNotificationChannel(ChannelsIds.REMINDER)
        return reminderChannel.importance != NotificationManager.IMPORTANCE_NONE
    }

    override fun openPrimaryPermissionSettings() {
        val intent = Intent().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            } else {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.parse("package:" + context.packageName)
            }

            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }

    override fun openReminderChannelPermissionSettings(channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
                addFlags(FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)
        }
    }


    private fun recreateReminderNotificationsChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.getNotificationChannel(ChannelsIds.REMINDER)?.let {
                return
            }

            val name = context.getString(R.string.reminders_channel_name)
            val descriptionText = context.getString(R.string.reminders_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channelId = ChannelsIds.REMINDER

            val mChannel = NotificationChannel(channelId, name, importance)
            mChannel.description = descriptionText

            notificationManager.createNotificationChannel(mChannel)
        }
    }


    object ChannelsIds {
        const val REMINDER = "reminder-notification-channel-id"

    }

}