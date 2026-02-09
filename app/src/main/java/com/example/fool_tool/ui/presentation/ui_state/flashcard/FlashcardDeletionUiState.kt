package com.example.fool_tool.ui.presentation.ui_state.flashcard

import androidx.compose.runtime.Immutable

@Immutable
sealed interface FlashcardDeletionUiState {
    data object NoSelection : FlashcardDeletionUiState
    data class Pending(val id: Long, val index: Int) : FlashcardDeletionUiState
    data class Ready(val id: Long, val index: Int) : FlashcardDeletionUiState
}