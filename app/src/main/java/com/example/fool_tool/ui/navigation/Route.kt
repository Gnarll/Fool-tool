package com.example.fool_tool.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


@Serializable
sealed interface Route : NavKey {
    @Serializable
    sealed interface BottomNavigationRoute : Route {
        @Serializable
        data object FlashcardRootRoute : BottomNavigationRoute

        @Serializable
        data object SmartnoteRootRoute : BottomNavigationRoute

        @Serializable
        data object ReminderRootRoute : BottomNavigationRoute

        @Serializable
        data object SettingsRootRoute : BottomNavigationRoute
    }


    @Serializable
    data object CreateFlashcardRoute : Route


    @Serializable
    data object CreateSmartnoteRoute : Route


    @Serializable
    data object CreateReminderRoute : Route

    @Serializable
    data class EditSmartnoteRoute(val id: Long) : Route
}