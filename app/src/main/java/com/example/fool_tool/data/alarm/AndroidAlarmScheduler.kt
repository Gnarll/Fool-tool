package com.example.fool_tool.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build
import android.provider.Settings
import com.example.fool_tool.broadcastReceivers.AlarmReceiver
import com.example.fool_tool.ui.model.Reminder
import com.example.fool_tool.utils.toMillisWithZone
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject


class AndroidAlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) :
    AlarmScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(reminder: Reminder): ScheduleResult {
        if (checkIsAlarmPermissionGranted()) {
            val serializedReminder = Json.encodeToString(Reminder.serializer(), reminder)

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra(EXTRA_REMINDER, serializedReminder)
            }

            val alarmTime = reminder.date.toMillisWithZone()
            val currentTime = System.currentTimeMillis()

            if (alarmTime < currentTime - 1000) {
                return ScheduleResult.FailedWithInvalidTime
            }

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                reminder.date.toMillisWithZone(),
                PendingIntent.getBroadcast(
                    context,
                    reminder.id.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )

            return ScheduleResult.Success
        } else {
            return ScheduleResult.FailedWithNoPermission
        }
    }

    override fun cancel(reminderId: Long) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                reminderId.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun checkIsAlarmPermissionGranted(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else true

    override fun openAlarmPermissionSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                addFlags(FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    companion object {
        const val EXTRA_REMINDER = "EXTRA_REMINDER"
    }
}