package com.example.fool_tool.di

import com.example.fool_tool.data.notifications.NotificationsService
import com.example.fool_tool.data.notifications.NotificationsServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface NotificationsServiceModule {
    @Binds
    fun bindNotificationsServiceImpl(notificationsServiceImpl: NotificationsServiceImpl): NotificationsService
}