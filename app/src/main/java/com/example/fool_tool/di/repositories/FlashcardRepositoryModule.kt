package com.example.fool_tool.di.repositories

import com.example.fool_tool.data.repository.FlashcardRepositoryImpl
import com.example.fool_tool.domain.repository.FlashcardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class FlashcardRepositoryModule {
    @Binds
    abstract fun bindFlashcardRepository(impl: FlashcardRepositoryImpl): FlashcardRepository
}