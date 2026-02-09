package com.example.fool_tool.core_android.model

import androidx.annotation.StringRes
import com.example.fool_tool.R

enum class Language(
    @StringRes val uiName: Int,
    val isoName: String,
    val selectable: Boolean = true
) {
    ENGLISH(uiName = R.string.english_lang, isoName = "en"),
    RUSSIAN(uiName = R.string.russian_lang, isoName = "ru"),
    SYSTEM(uiName = R.string.system_lang, isoName = "_", selectable = false);

    companion object {

        fun getFromString(string: String?): Language =
            entries.find { it.isoName == string } ?: SYSTEM

    }
}