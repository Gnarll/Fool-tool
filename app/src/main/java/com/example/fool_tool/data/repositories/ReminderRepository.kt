package com.example.fool_tool.data.repositories

import com.example.fool_tool.data.local.dao.ReminderDao
import com.example.fool_tool.data.local.entities.toReminder
import com.example.fool_tool.ui.model.Reminder
import com.example.fool_tool.ui.model.toReminderEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ReminderRepository {
    fun getAllRemindersStream(): Flow<List<Reminder>>
    suspend fun updateReminder(reminder: Reminder)
    suspend fun deleteReminder(reminder: Reminder)
}

class ReminderRepositoryImpl @Inject constructor(private val reminderDao: ReminderDao) :
    ReminderRepository {
    override fun getAllRemindersStream(): Flow<List<Reminder>> =
        reminderDao.getAll().map { reminderEntities ->
            reminderEntities.map {
                it.toReminder()
            }
        }

    override suspend fun updateReminder(reminder: Reminder) {
        reminderDao.update(reminder.toReminderEntity())
    }

    override suspend fun deleteReminder(reminder: Reminder) {
        reminderDao.delete(reminder.toReminderEntity())
    }
}