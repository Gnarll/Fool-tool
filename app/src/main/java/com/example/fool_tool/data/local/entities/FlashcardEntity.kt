package com.example.fool_tool.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fool_tool.ui.model.Flashcard

@Entity(tableName = "flashcard")
data class FlashcardEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Long,
    @ColumnInfo(name = "native_word")
    val nativeWord: String,
    @ColumnInfo(name = "foreign_word")
    val foreignWord: String
)

fun FlashcardEntity.toFlashcard() =
    Flashcard(id = uid, foreignWord = foreignWord, nativeWord = nativeWord)