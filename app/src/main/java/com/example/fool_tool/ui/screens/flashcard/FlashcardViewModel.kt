package com.example.fool_tool.ui.screens.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fool_tool.data.repositories.FlashcardRepository
import com.example.fool_tool.ui.UiState
import com.example.fool_tool.ui.model.Flashcard
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


sealed interface FlashcardDeletionState {
    object NoSelection : FlashcardDeletionState
    class Pending(val id: Long, val index: Int) : FlashcardDeletionState
    class Ready(val id: Long, val index: Int) : FlashcardDeletionState
}

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

    private var _flashcardDeletionState: MutableStateFlow<FlashcardDeletionState> =
        MutableStateFlow(FlashcardDeletionState.NoSelection)

    val flashcardDeletionState: StateFlow<FlashcardDeletionState> =
        _flashcardDeletionState.asStateFlow()

    fun setFlashcardDeletionState(state: FlashcardDeletionState) {
        _flashcardDeletionState.update {
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

