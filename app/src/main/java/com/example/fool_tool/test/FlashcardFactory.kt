package com.example.fool_tool.test

import com.example.fool_tool.data.model.FlashcardEntity
import com.example.fool_tool.domain.model.Flashcard
import java.util.UUID

object FlashcardFactory {
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