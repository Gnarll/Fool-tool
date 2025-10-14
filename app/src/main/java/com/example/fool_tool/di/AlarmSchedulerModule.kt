package com.example.fool_tool.di

import com.example.fool_tool.data.alarm.AlarmScheduler
import com.example.fool_tool.data.alarm.AndroidAlarmScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AlarmSchedulerModule {
    @Binds
    @Singleton
    fun bindAlarmScheduler(scheduler: AndroidAlarmScheduler): AlarmScheduler
}