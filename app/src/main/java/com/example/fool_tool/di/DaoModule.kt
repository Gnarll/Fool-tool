package com.example.fool_tool.di

import com.example.fool_tool.data.local.dao.FlashcardDao
import com.example.fool_tool.data.local.dao.ReminderDao
import com.example.fool_tool.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    @Singleton
    fun provideFlashcardDao(db: AppDatabase): FlashcardDao = db.flashcardDao()

    @Provides
    @Singleton
    fun provideReminderDao(db: AppDatabase): ReminderDao = db.reminderDao()
}