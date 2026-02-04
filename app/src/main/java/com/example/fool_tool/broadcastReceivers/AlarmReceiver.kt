package com.example.fool_tool.broadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.fool_tool.data.notifications.NotificationsService
import com.example.fool_tool.data.repositories.ReminderRepository
import com.example.fool_tool.di.qualifiers.ExtraReminder
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

    @Inject
    lateinit var notificationsService: NotificationsService

    @Inject
    @ExtraReminder
    lateinit var extraReminder: String

    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
        if (context != null && intent != null) {
            val serializedReminder = intent.getStringExtra(extraReminder)

            serializedReminder?.let {
                val reminder = Json.decodeFromString(Reminder.serializer(), it)

                notificationsService.sendReminderNotification(reminder)
                reminderRepository.updateReminder(reminder.copy(status = ReminderStatus.SUCCEED))

            }

        }

    }
}