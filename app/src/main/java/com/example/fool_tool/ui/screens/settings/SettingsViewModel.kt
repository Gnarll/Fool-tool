package com.example.fool_tool.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fool_tool.data.local.DataStoreManager
import com.example.fool_tool.data.local.LocaleManager
import com.example.fool_tool.ui.components.settings.Theme
import com.example.fool_tool.utils.Language
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val localeManager: LocaleManager
) :
    ViewModel() {
    val theme: StateFlow<Theme> =
        dataStoreManager.userThemePreferencesFlow
            .map { userThemePreferences ->
                when (userThemePreferences.isDarkTheme) {
                    true -> Theme.DARK_THEME
                    false -> Theme.LIGHT_THEME
                    else -> Theme.SYSTEM_THEME
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = Theme.SYSTEM_THEME
            )


    fun setTheme(theme: Theme) {
        viewModelScope.launch {
            when (theme) {
                Theme.DARK_THEME -> {
                    dataStoreManager.setDesiredTheme(isDark = true)
                }

                Theme.LIGHT_THEME -> {
                    dataStoreManager.setDesiredTheme(isDark = false)
                }

                else -> {
                    dataStoreManager.setSystemTheme()
                }
            }
        }
    }

    val language = localeManager.language

    fun setLanguage(language: Language) {
        localeManager.setLanguage(language)
    }
}