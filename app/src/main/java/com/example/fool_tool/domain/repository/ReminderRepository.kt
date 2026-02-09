package com.example.fool_tool.domain.repository

import androidx.paging.PagingData
import com.example.fool_tool.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun getPagedReminders(): Flow<PagingData<Reminder>>

    suspend fun getReminderOffset(id: Long): Int?
    suspend fun getReminder(id: Long): Reminder
    suspend fun getPendingReminders(): List<Reminder>
    suspend fun createReminder(reminder: Reminder)
    suspend fun updateReminder(reminder: Reminder)
    suspend fun deleteReminder(id: Long)
}