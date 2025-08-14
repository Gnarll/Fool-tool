package com.example.fool_tool.di

import com.example.fool_tool.data.repositories.FlashcardRepository
import com.example.fool_tool.data.repositories.FlashcardRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class FlashcardRepositoryModule {
    @ActivityRetainedScoped
    @Binds
    abstract fun bindFlashcardRepository(impl: FlashcardRepositoryImpl): FlashcardRepository
}