package com.example.fool_tool

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fool_tool.data.local.DataStoreManager
import com.example.fool_tool.data.local.UserThemePreferences
import com.example.fool_tool.ui.navigation.RootNavigator
import com.example.fool_tool.ui.theme.FooltoolTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userThemePreferences =
                dataStoreManager.userThemePreferencesFlow.collectAsStateWithLifecycle(
                    initialValue = UserThemePreferences(
                        null
                    )
                )
            val darkTheme = userThemePreferences.value.isDarkTheme ?: isSystemInDarkTheme()

            FooltoolTheme(darkTheme = darkTheme) {
                RootNavigator()
            }
        }
    }
}
