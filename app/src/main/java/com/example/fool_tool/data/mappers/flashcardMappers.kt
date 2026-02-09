package com.example.fool_tool.data.mappers

import com.example.fool_tool.data.model.FlashcardEntity
import com.example.fool_tool.domain.model.Flashcard

fun FlashcardEntity.toFlashcard() =
    Flashcard(id = uid, foreignWord = foreignWord, nativeWord = nativeWord)

fun Flashcard.toFlashcardEntity() =
    FlashcardEntity(uid = id, nativeWord = nativeWord, foreignWord = foreignWord)