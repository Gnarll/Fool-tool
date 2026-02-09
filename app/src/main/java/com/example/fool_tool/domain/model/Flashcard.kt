package com.example.fool_tool.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Flashcard(
    val id: Long,
    val foreignWord: String,
    val nativeWord: String
)

