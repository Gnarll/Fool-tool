package com.example.fool_tool.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fool_tool.data.local.dao.FlashcardDao
import com.example.fool_tool.data.local.entities.FlashcardEntity

@Database(entities = [FlashcardEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao

    companion object {
        const val DB_NAME = "fool-tool-database"
    }
}