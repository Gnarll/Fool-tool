package com.example.fool_tool.core_android.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build
import android.provider.Settings
import com.example.fool_tool.core.extensions.toMillisWithZone
import com.example.fool_tool.di.qualifiers.ExtraReminder
import com.example.fool_tool.domain.model.Reminder
import com.example.fool_tool.domain.model.ScheduleResult
import com.example.fool_tool.receivers.AlarmReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AlarmSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @ExtraReminder private val extraReminder: String
) :
    AlarmScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(reminder: Reminder): ScheduleResult {
        if (checkIsAlarmPermissionGranted()) {
            val serializedReminder = Json.encodeToString(Reminder.serializer(), reminder)

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra(extraReminder, serializedReminder)
            }

            val alarmTime = reminder.date.toMillisWithZone()
            val currentTime = System.currentTimeMillis()

            if (alarmTime < currentTime + 200L) {
                return ScheduleResult.FailedWithInvalidTime
            }

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTime,
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

}