package com.example.fool_tool.data.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.example.fool_tool.MainActivity
import com.example.fool_tool.R
import com.example.fool_tool.ui.model.Reminder
import com.example.fool_tool.utils.toShortNotificationContentText
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

    override fun sendReminderNotification(reminder: Reminder) {
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
        }
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                reminder.id.toInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

        val notification = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_calendar)
            .setContentTitle(reminder.title)
            .setContentText(reminder.description.toShortNotificationContentText())
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(reminder.description)
            )
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()


        notificationManager.notify(reminder.id.toInt(), notification)
    }

    override fun checkIsPrimaryPermissionGranted(): Boolean =
        notificationManager.areNotificationsEnabled()

    override fun checkIsReminderChannelPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return true

        val reminderChannel = notificationManager.getNotificationChannel(REMINDER_CHANNEL_ID)
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

    override fun openReminderChannelPermissionSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                putExtra(Settings.EXTRA_CHANNEL_ID, REMINDER_CHANNEL_ID)
                addFlags(FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)
        }
    }


    private fun recreateReminderNotificationsChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.getNotificationChannel(REMINDER_CHANNEL_ID)?.let {
                return
            }

            val name = context.getString(R.string.reminders_channel_name)
            val descriptionText = context.getString(R.string.reminders_channel_description)
            val importance = NotificationManager.IMPORTANCE_MAX
            val channelId = REMINDER_CHANNEL_ID

            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
                setShowBadge(false)
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val REMINDER_CHANNEL_ID = "reminder-notification-channel-id"
    }

}