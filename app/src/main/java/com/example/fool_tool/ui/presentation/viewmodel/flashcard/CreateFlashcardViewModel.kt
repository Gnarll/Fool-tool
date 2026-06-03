package com.example.fool_tool.ui.presentation.viewmodel.flashcard

import androidx.lifecycle.ViewModel
import com.example.fool_tool.domain.model.factory.FlashcardFactory
import com.example.fool_tool.domain.repository.FlashcardRepository
import com.example.fool_tool.domain.validation.FlashcardValidator
import com.example.fool_tool.domain.validation.ValidationError
import com.example.fool_tool.ui.presentation.ui_state.flashcard.CreateFlashcardFormUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class CreateFlashcardViewModel @Inject constructor(
    private val flashcardRepository: FlashcardRepository,
    private val validator: FlashcardValidator
) :
    ViewModel() {
    private var _uiState = MutableStateFlow(
        CreateFlashcardFormUiState(
            nativeWordError = ValidationError.EmptyInputError,
            foreignWordError = ValidationError.EmptyInputError
        )
    )
    val uiState: StateFlow<CreateFlashcardFormUiState> =
        _uiState.asStateFlow()

    fun onNativeWordChange(nativeWord: String) {
        val error = validator.validateNativeWord(nativeWord)
        _uiState.update {
            it.copy(
                nativeWord = nativeWord,
                nativeWordError = error
            )
        }
    }

    fun onForeignWordChange(foreignWord: String) {
        val error = validator.validateForeignWord(foreignWord)
        _uiState.update {
            it.copy(
                foreignWord = foreignWord,
                foreignWordError = error
            )
        }
    }

    suspend fun createFlashcard() {
        val flashcard = FlashcardFactory.createFlashcard(
            nativeWord = _uiState.value.nativeWord,
            foreignWord = _uiState.value.foreignWord
        )
        flashcardRepository.insertFlashcard(flashcard)
    }
}