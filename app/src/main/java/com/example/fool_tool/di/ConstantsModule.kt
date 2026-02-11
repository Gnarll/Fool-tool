package com.example.fool_tool.di

import androidx.paging.PagingConfig
import com.example.fool_tool.di.qualifiers.ExtraReminder
import com.example.fool_tool.di.qualifiers.ExtraReminderId
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ConstantsModule {

    @Provides
    @Singleton
    fun provideReminderValidationConstants(): ReminderValidationConstants =
        ReminderValidationConstants(TITLE_MAX_SYMBOLS, DESCRIPTION_MAX_SYMBOLS)

    @Provides
    @ExtraReminder
    @Singleton
    fun provideExtraReminder(): String = EXTRA_REMINDER

    @Provides
    @ExtraReminderId
    @Singleton
    fun provideExtraReminderId(): String = EXTRA_REMINDER_ID

    @Provides
    @Singleton
    fun provideReminderPagingConfig(): PagingConfig = REMINDER_PAGING_CONFIG


    private const val TITLE_MAX_SYMBOLS = 35
    private const val DESCRIPTION_MAX_SYMBOLS = 80
    private const val EXTRA_REMINDER = "EXTRA_REMINDER"
    private const val EXTRA_REMINDER_ID = "EXTRA_REMINDER_ID"

    private val REMINDER_PAGING_CONFIG = PagingConfig(
        pageSize = 20,
        initialLoadSize = 40,
        prefetchDistance = 10,
        enablePlaceholders = false
    )
}


data class ReminderValidationConstants(val titleMaxSymbols: Int, val descriptionMaxSymbols: Int)