package com.example.fool_tool.ui.model

import com.example.fool_tool.data.local.entities.FlashcardEntity

data class Flashcard(val id: Long, val foreignWord: String, val nativeWord: String)

fun Flashcard.toFlashcardEntity() =
    FlashcardEntity(uid = id, nativeWord = nativeWord, foreignWord = foreignWord)