package com.example.fool_tool.data.repositories.di

import com.example.fool_tool.data.local.dao.FlashcardDao
import com.example.fool_tool.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FlashcardDaoModule {
    @Provides
    @Singleton
    fun provideFlashcardDao(db: AppDatabase): FlashcardDao = db.flashcardDao()
}