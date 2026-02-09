package com.example.fool_tool.ui.presentation.viewmodel.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fool_tool.domain.model.Flashcard
import com.example.fool_tool.domain.repository.FlashcardRepository
import com.example.fool_tool.ui.presentation.ui_state.UiState
import com.example.fool_tool.ui.presentation.ui_state.flashcard.FlashcardDeletionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlashcardViewModel @Inject constructor(
    private val flashcardRepository: FlashcardRepository,
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

    private var _flashcardDeletionUiState: MutableStateFlow<FlashcardDeletionUiState> =
        MutableStateFlow(FlashcardDeletionUiState.NoSelection)

    val flashcardDeletionUiState: StateFlow<FlashcardDeletionUiState> =
        _flashcardDeletionUiState.asStateFlow()

    fun setFlashcardDeletionState(state: FlashcardDeletionUiState) {
        _flashcardDeletionUiState.update {
            state
        }
    }

    fun deleteFlashcard(flashcard: Flashcard) {
        viewModelScope.launch {
            flashcardRepository.deleteFlashcard(flashcard)
        }
    }

    fun deleteFlashcardById(id: Long) {
        viewModelScope.launch {
            flashcardRepository.deleteFlashcardById(id)
        }
    }
}

