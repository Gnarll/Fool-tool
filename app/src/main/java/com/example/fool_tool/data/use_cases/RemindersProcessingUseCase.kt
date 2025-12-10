package com.example.fool_tool.data.use_cases

interface RemindersProcessingUseCase {
    suspend fun reactivateOrDeclinePendingReminders()
}