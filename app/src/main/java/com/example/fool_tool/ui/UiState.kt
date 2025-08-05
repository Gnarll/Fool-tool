package com.example.fool_tool.ui

sealed interface UiState<out T> {

    val isLoading: Boolean

    data class Success<T>(val value: T, override val isLoading: Boolean = false) : UiState<T>

    data object Loading : UiState<Nothing> {
        override val isLoading: Boolean = true
    }

    data class Failure<T>(val throwable: Throwable) : UiState<T> {
        override val isLoading: Boolean = false
    }
}