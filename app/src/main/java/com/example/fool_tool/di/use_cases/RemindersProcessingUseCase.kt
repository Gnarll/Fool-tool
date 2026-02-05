package com.example.fool_tool.di.use_cases

import com.example.fool_tool.data.use_cases.RemindersProcessingUseCase
import com.example.fool_tool.data.use_cases.RemindersProcessingUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RemindersProcessingUseCaseModule {
    @Binds
    @Singleton
    fun bindRemindersProcessingUseCase(
        remindersProcessingUseCase: RemindersProcessingUseCaseImpl
    ): RemindersProcessingUseCase
}