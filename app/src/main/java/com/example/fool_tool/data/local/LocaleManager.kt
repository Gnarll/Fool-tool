package com.example.fool_tool.data.local

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import com.example.fool_tool.utils.Language
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LocaleManager @Inject constructor(@ApplicationContext private val context: Context) {
    private var _language = MutableStateFlow(Language.DEFAULT)
    val language = _language.asStateFlow()

    init {
        val appLocales = AppCompatDelegate.getApplicationLocales()
        val firstLocaleTag =
            appLocales.toLanguageTags().split(",").firstOrNull()?.takeIf { it.isNotBlank() }

        val firstLocale: Locale? = if (firstLocaleTag != null) {
            Locale.forLanguageTag(firstLocaleTag)
        } else {
            val resources: Resources = context.resources
            val configuration: Configuration = resources.configuration
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.locales.get(0)
            } else {
                ConfigurationCompat.getLocales(configuration).get(0)
            }
        }

        val currentLanguageCode = firstLocale?.language
        _language.value = Language.getFromString(currentLanguageCode)
    }

    fun setLanguage(language: Language) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(language.isoName)
        )
        _language.value = language
    }
}