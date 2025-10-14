package com.example.fool_tool.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.fool_tool.data.local.dao.ReminderDao
import com.example.fool_tool.data.local.entities.toReminder
import com.example.fool_tool.ui.model.Reminder
import com.example.fool_tool.ui.model.ReminderStatus
import com.example.fool_tool.ui.model.toReminderEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ReminderRepository {
    fun getPagedReminders(): Flow<PagingData<Reminder>>

    suspend fun getPendingReminders(): List<Reminder>
    suspend fun createReminder(reminder: Reminder)
    suspend fun updateReminder(reminder: Reminder)
    suspend fun deleteReminder(id: Long)
}

class ReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao,
) :
    ReminderRepository {
    override fun getPagedReminders(): Flow<PagingData<Reminder>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 40,
                prefetchDistance = 10,
                enablePlaceholders = false
            )
        ) {
            reminderDao.getPagingSource()
        }.flow
            .map { pagingData ->
                pagingData.map { reminderEntity ->
                    reminderEntity.toReminder()
                }
            }
    }

    override suspend fun getPendingReminders(): List<Reminder> {
        val reminders = reminderDao.getRemindersByStatus(ReminderStatus.PENDING)
        return reminders.map { it.toReminder() }
    }


    override suspend fun createReminder(reminder: Reminder) {
        reminderDao.insert(reminder.toReminderEntity())
    }

    override suspend fun updateReminder(reminder: Reminder) {
        reminderDao.update(reminder.toReminderEntity())
    }

    override suspend fun deleteReminder(id: Long) {
        reminderDao.delete(id)
    }
}