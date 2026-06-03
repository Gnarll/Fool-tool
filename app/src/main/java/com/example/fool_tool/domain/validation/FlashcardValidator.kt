package com.example.fool_tool.domain.validation

import javax.inject.Inject

class FlashcardValidator @Inject constructor() {
    fun validateNativeWord(word: String): ValidationError? {
        return when {
            word.isEmpty() -> ValidationError.EmptyInputError
            else -> null
        }
    }

    fun validateForeignWord(word: String): ValidationError? {
        return when {
            word.isEmpty() -> ValidationError.EmptyInputError
            else -> null
        }
    }
}