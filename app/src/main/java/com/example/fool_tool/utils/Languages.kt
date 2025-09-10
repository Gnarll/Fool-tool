package com.example.fool_tool.utils

import androidx.annotation.StringRes
import com.example.fool_tool.R

enum class Language(@StringRes val uiName: Int, val isoName: String) {
    ENGLISH(uiName = R.string.english_lang, isoName = "en_US"),
    RUSSIAN(uiName = R.string.russian_lang, isoName = "ru");

    companion object {
        val DEFAULT = ENGLISH

        fun getFromString(string: String?): Language =
            entries.find { it.isoName == string } ?: DEFAULT

    }
}