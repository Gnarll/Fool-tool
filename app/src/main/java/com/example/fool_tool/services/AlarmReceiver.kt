package com.example.fool_tool.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.fool_tool.data.alarm.AndroidAlarmScheduler
import com.example.fool_tool.data.repositories.ReminderRepository
import com.example.fool_tool.ui.model.Reminder
import com.example.fool_tool.ui.model.ReminderStatus
import com.example.fool_tool.utils.goAsync
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    @Inject
    lateinit var reminderRepository: ReminderRepository

    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
        if (context != null && intent != null) {
            val serializedReminder = intent.getStringExtra(AndroidAlarmScheduler.EXTRA_REMINDER)

            serializedReminder?.let {
                val reminder = Json.decodeFromString(Reminder.serializer(), it)

                // TODO send notification

                reminderRepository.updateReminder(reminder.copy(status = ReminderStatus.SUCCEED))

            }

        }

    }
}