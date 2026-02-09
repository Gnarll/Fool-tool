package com.example.fool_tool.di

import com.example.fool_tool.core_android.alarm.AlarmScheduler
import com.example.fool_tool.core_android.alarm.AlarmSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AlarmSchedulerModule {
    @Binds
    fun bindAlarmScheduler(scheduler: AlarmSchedulerImpl): AlarmScheduler
}