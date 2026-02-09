package com.example.fool_tool.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcard")
data class FlashcardEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Long,
    @ColumnInfo(name = "native_word")
    val nativeWord: String,
    @ColumnInfo(name = "foreign_word")
    val foreignWord: String
)

