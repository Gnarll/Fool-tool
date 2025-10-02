package com.example.fool_tool.ui.screens.flashcard

import androidx.lifecycle.ViewModel
import com.example.fool_tool.data.repositories.FlashcardRepository
import com.example.fool_tool.utils.EmptyInputError
import com.example.fool_tool.utils.FlashcardCreating
import com.example.fool_tool.utils.ValidationError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


data class CreateFlashcardFormState(
    val nativeWord: String = "",
    val foreignWord: String = "",
    val nativeWordError: ValidationError? = null,
    val foreignWordError: ValidationError? = null
)

@HiltViewModel
class CreateFlashcardViewModel @Inject constructor(private val flashcardRepository: FlashcardRepository) :
    ViewModel() {
    private var _createFlashcardFormState = MutableStateFlow(
        CreateFlashcardFormState(
            nativeWordError = EmptyInputError,
            foreignWordError = EmptyInputError
        )
    )
    val createFlashcardFormState: StateFlow<CreateFlashcardFormState> =
        _createFlashcardFormState.asStateFlow()

    fun onNativeWordChange(nativeWord: String) {
        _createFlashcardFormState.update {
            it.copy(
                nativeWord = nativeWord,
                nativeWordError = validateNativeWord(nativeWord)
            )
        }
    }

    fun onForeignWordChange(foreignWord: String) {
        _createFlashcardFormState.update {
            it.copy(
                foreignWord = foreignWord,
                foreignWordError = validateForeignWord(foreignWord)
            )
        }
    }

    suspend fun createFlashcard() {
        val flashcard = FlashcardCreating.createFlashcard(
            nativeWord = _createFlashcardFormState.value.nativeWord,
            foreignWord = _createFlashcardFormState.value.foreignWord
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