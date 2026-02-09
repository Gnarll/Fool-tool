package com.example.fool_tool.di.repositories

import com.example.fool_tool.data.repository.ReminderRepositoryImpl
import com.example.fool_tool.domain.repository.ReminderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ReminderRepositoryModule {
    @Binds
    fun bindReminderRepository(impl: ReminderRepositoryImpl): ReminderRepository
}