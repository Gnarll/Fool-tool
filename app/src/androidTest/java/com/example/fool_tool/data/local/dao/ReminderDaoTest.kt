package com.example.fool_tool.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.fool_tool.core.extensions.toLocalDateTimeWithZone
import com.example.fool_tool.core.extensions.toMillisWithZone
import com.example.fool_tool.data.local.db.AppDatabase
import com.example.fool_tool.test.ReminderFactory
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNull

@RunWith(AndroidJUnit4::class)
class ReminderDaoTest(
) {
    lateinit var db: AppDatabase
    lateinit var dao: ReminderDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.reminderDao()

    }

    @After
    fun tearDown() {
        db.close()
    }


    @Test
    fun getReminderOffset_returns_correct_row_number_by_date_desc_and_uid() = runTest {
        val reminders = (1L..10L).map {
            val perItemDateMillisOffset = it * 100
            val dateInMillis = LocalDateTime.now().toMillisWithZone()
            val date = (dateInMillis - perItemDateMillisOffset).toLocalDateTimeWithZone()

            ReminderFactory.createReminderEntity(id = it, date = date)
        }

        dao.insertMany(reminders)

        assertNull(dao.getReminderOffset(666L))
        assertEquals(dao.getReminderOffset(1L), 0)
        assertEquals(dao.getReminderOffset(10L), 9)
    }
}