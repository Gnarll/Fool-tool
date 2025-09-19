package com.example.fool_tool.di.repositories

import com.example.fool_tool.data.repositories.ReminderRepository
import com.example.fool_tool.data.repositories.ReminderRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
interface ReminderRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    fun bindReminderRepository(impl: ReminderRepositoryImpl): ReminderRepository
}