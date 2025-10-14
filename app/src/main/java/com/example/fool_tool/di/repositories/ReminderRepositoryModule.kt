package com.example.fool_tool.di.repositories

import com.example.fool_tool.data.repositories.ReminderRepository
import com.example.fool_tool.data.repositories.ReminderRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ReminderRepositoryModule {
    @Binds
    @Singleton
    fun bindReminderRepository(impl: ReminderRepositoryImpl): ReminderRepository
}