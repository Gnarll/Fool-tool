package com.example.fool_tool.data.mappers

import com.example.fool_tool.data.model.ReminderEntity
import com.example.fool_tool.domain.model.Reminder

fun ReminderEntity.toReminder() =
    Reminder(id = uid, date = date, title = title, description = description, status = status)

fun Reminder.toReminderEntity() =
    ReminderEntity(uid = id, date = date, title = title, description = description, status = status)