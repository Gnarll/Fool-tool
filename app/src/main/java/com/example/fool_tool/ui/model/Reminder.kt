package com.example.fool_tool.ui.model

import com.example.fool_tool.data.local.entities.ReminderEntity
import com.example.fool_tool.utils.DateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

enum class ReminderStatus {
    PENDING,
    SUCCEED,
    CANCELLED,
    DENIED
}


@Serializable()
data class Reminder(
    val id: Long,
    @Serializable(with = DateTimeSerializer::class)
    val date: LocalDateTime,
    val title: String,
    val description: String,
    val status: ReminderStatus
)


fun Reminder.toReminderEntity() =
    ReminderEntity(uid = id, date = date, title = title, description = description, status = status)