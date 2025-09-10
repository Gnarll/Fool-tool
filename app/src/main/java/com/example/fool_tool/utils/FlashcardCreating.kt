package com.example.fool_tool.utils

import com.example.fool_tool.data.local.entities.FlashcardEntity
import com.example.fool_tool.ui.model.Flashcard
import java.util.UUID

object FlashcardCreating {
    fun createFlashcardEntity(
        nativeWord: String = "Test",
        foreignWord: String = "Test",
        id: Long = UUID.randomUUID().mostSignificantBits
    ): FlashcardEntity =
        FlashcardEntity(uid = id, nativeWord = nativeWord, foreignWord = foreignWord)

    fun createFlashcard(
        nativeWord: String = "Test",
        foreignWord: String = "Test",
        id: Long = UUID.randomUUID().mostSignificantBits
    ): Flashcard =
        Flashcard(id = id, nativeWord = nativeWord, foreignWord = foreignWord)
}