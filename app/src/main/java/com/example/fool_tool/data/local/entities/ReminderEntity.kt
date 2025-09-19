package com.example.fool_tool.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fool_tool.ui.model.Reminder
import com.example.fool_tool.ui.model.ReminderStatus
import java.time.LocalDateTime

@Entity(tableName = "reminder")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Long,
    @ColumnInfo("date")
    val date: LocalDateTime,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("description")
    val description: String,
    @ColumnInfo("status")
    val status: ReminderStatus
)

fun ReminderEntity.toReminder() =
    Reminder(id = uid, date = date, title = title, description = description, status = status)