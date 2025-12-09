package com.example.fool_tool.ui.model

import androidx.compose.runtime.Immutable
import com.example.fool_tool.data.local.entities.FlashcardEntity

@Immutable
data class Flashcard(val id: Long, val foreignWord: String, val nativeWord: String)

fun Flashcard.toFlashcardEntity() =
    FlashcardEntity(uid = id, nativeWord = nativeWord, foreignWord = foreignWord)