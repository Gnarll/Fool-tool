package com.example.fool_tool.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.fool_tool.domain.model.UserThemePreferences
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Singleton
class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {

    suspend fun setDesiredTheme(isDark: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDark
        }
    }

    suspend fun setSystemTheme() {
        dataStore.edit { preferences ->
            preferences.remove(THEME_KEY)
        }
    }

    val userThemePreferencesFlow: Flow<UserThemePreferences> = dataStore.data.map { preferences ->
        UserThemePreferences(preferences[THEME_KEY])
    }.distinctUntilChanged()

    companion object {
        private val THEME_KEY: Preferences.Key<Boolean> = booleanPreferencesKey("theme_key")
    }
}