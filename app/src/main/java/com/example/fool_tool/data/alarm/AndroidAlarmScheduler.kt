package com.example.fool_tool.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.fool_tool.services.AlarmReceiver
import com.example.fool_tool.ui.model.Reminder
import com.example.fool_tool.utils.toMillisWithZone
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class AndroidAlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) :
    AlarmScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(reminder: Reminder): ScheduleResult {
        if (checkIsAlarmPermissionGranted()) {
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra(EXTRA_TITLE, reminder.title)
                putExtra(EXTRA_DESCRIPTION, reminder.description)
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

    override fun cancel(reminder: Reminder) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                reminder.id.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun checkIsAlarmPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else true
    }

    companion object {
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"

    }
}