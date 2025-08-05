package com.example.fool_tool.ui.screens.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fool_tool.data.repositories.FlashcardRepository
import com.example.fool_tool.ui.UiState
import com.example.fool_tool.ui.model.Flashcard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FlashcardViewModel @Inject constructor(
    val flashcardRepository: FlashcardRepository,
) :
    ViewModel() {

    val flashcardStateFlow: StateFlow<UiState<List<Flashcard>>> =
        flashcardRepository.getAllFlashcardsStream()
            .map { flashcards ->
                UiState.Success(flashcards) as UiState<List<Flashcard>>
            }
            .catch { e ->
                emit(UiState.Failure(e))
            }
            .stateIn(
                initialValue = UiState.Loading,
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L)
            )
}

