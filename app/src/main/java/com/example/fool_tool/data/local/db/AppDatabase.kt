package com.example.fool_tool.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fool_tool.data.local.Converters
import com.example.fool_tool.data.local.dao.FlashcardDao
import com.example.fool_tool.data.local.dao.ReminderDao
import com.example.fool_tool.data.local.entities.FlashcardEntity
import com.example.fool_tool.data.local.entities.ReminderEntity

@Database(
    entities = [FlashcardEntity::class, ReminderEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao

    abstract fun reminderDao(): ReminderDao

    companion object {
        const val DB_NAME = "fool-tool-database"
    }
}