package com.example.fool_tool

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.fool_tool.data.local.DataStoreManager
import com.example.fool_tool.data.local.UserThemePreferences
import com.example.fool_tool.di.qualifiers.ExtraReminderId
import com.example.fool_tool.ui.navigation.BackStack
import com.example.fool_tool.ui.navigation.RootNavigator
import com.example.fool_tool.ui.navigation.Route
import com.example.fool_tool.ui.theme.FooltoolTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    @ExtraReminderId
    lateinit var extraReminderId: String

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


            val initialRoute = getInitialBackStackElements()
            val backStack: BackStack =
                rememberNavBackStack(*initialRoute)

            FooltoolTheme(darkTheme = darkTheme) {
                RootNavigator(backStack = backStack)
            }
        }
    }

    private fun getInitialBackStackElements(): Array<Route> {
        val defaultValue = -1L
        val reminderIdFromNotification = intent.getLongExtra(extraReminderId, defaultValue)

        return when {
            reminderIdFromNotification == defaultValue -> {
                arrayOf(Route.BottomNavigationRoute.FlashcardRootRoute)
            }

            else -> {
                arrayOf(
                    Route.BottomNavigationRoute.FlashcardRootRoute,
                    Route.BottomNavigationRoute.ReminderRootRoute(reminderIdFromNotification)
                )
            }
        }
    }
}
