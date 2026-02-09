package com.example.fool_tool.ui.presentation.viewmodel.flashcard

import androidx.lifecycle.ViewModel
import com.example.fool_tool.core_android.util.validation.EmptyInputError
import com.example.fool_tool.core_android.util.validation.ValidationError
import com.example.fool_tool.domain.repository.FlashcardRepository
import com.example.fool_tool.test.FlashcardFactory
import com.example.fool_tool.ui.presentation.ui_state.flashcard.CreateFlashcardFormUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class CreateFlashcardViewModel @Inject constructor(private val flashcardRepository: FlashcardRepository) :
    ViewModel() {
    private var _createFlashcardFormUiState = MutableStateFlow(
        CreateFlashcardFormUiState(
            nativeWordError = EmptyInputError,
            foreignWordError = EmptyInputError
        )
    )
    val createFlashcardFormUiState: StateFlow<CreateFlashcardFormUiState> =
        _createFlashcardFormUiState.asStateFlow()

    fun onNativeWordChange(nativeWord: String) {
        _createFlashcardFormUiState.update {
            it.copy(
                nativeWord = nativeWord,
                nativeWordError = validateNativeWord(nativeWord)
            )
        }
    }

    fun onForeignWordChange(foreignWord: String) {
        _createFlashcardFormUiState.update {
            it.copy(
                foreignWord = foreignWord,
                foreignWordError = validateForeignWord(foreignWord)
            )
        }
    }

    suspend fun createFlashcard() {
        val flashcard = FlashcardFactory.createFlashcard(
            nativeWord = _createFlashcardFormUiState.value.nativeWord,
            foreignWord = _createFlashcardFormUiState.value.foreignWord
        )
        flashcardRepository.insertFlashcard(flashcard)
    }

    private fun validateNativeWord(nativeWord: String): ValidationError? = when {
        nativeWord.isEmpty() -> EmptyInputError
        else -> null
    }

    private fun validateForeignWord(foreignWord: String): ValidationError? = when {
        foreignWord.isEmpty() -> EmptyInputError
        else -> null
    }
}