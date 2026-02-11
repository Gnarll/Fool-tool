package com.example.fool_tool.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.fool_tool.data.local.dao.ReminderDao
import com.example.fool_tool.data.mappers.toReminder
import com.example.fool_tool.data.mappers.toReminderEntity
import com.example.fool_tool.domain.model.Reminder
import com.example.fool_tool.domain.model.ReminderStatus
import com.example.fool_tool.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao,
    private val reminderPagingConfig: PagingConfig
) :
    ReminderRepository {
    override fun getPagedReminders(): Flow<PagingData<Reminder>> {
        return Pager(
            config = reminderPagingConfig
        ) {
            reminderDao.getPagingSource()
        }.flow
            .map { pagingData ->
                pagingData.map { reminderEntity ->
                    reminderEntity.toReminder()
                }
            }
    }

    override suspend fun getReminderOffset(id: Long): Int? =
        reminderDao.getReminderOffset(id)


    override suspend fun getReminderById(id: Long): Reminder =
        reminderDao.getReminderById(id).toReminder()


    override suspend fun getPendingReminders(): List<Reminder> {
        val reminders = reminderDao.getRemindersByStatus(ReminderStatus.PENDING)
        return reminders.map { it.toReminder() }
    }


    override suspend fun insertReminder(reminder: Reminder) {
        reminderDao.insert(reminder.toReminderEntity())
    }

    override suspend fun updateReminder(reminder: Reminder) {
        reminderDao.update(reminder.toReminderEntity())
    }

    override suspend fun deleteReminder(id: Long) {
        reminderDao.delete(id)
    }
}