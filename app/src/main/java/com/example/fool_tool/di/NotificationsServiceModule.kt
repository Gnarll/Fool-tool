package com.example.fool_tool.di

import com.example.fool_tool.core_android.notification.NotificationService
import com.example.fool_tool.core_android.notification.NotificationServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface NotificationsServiceModule {
    @Binds
    fun bindNotificationsServiceImpl(notificationsServiceImpl: NotificationServiceImpl): NotificationService
}