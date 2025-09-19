package com.example.fool_tool.ui.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed interface Route {
    @Serializable
    sealed class BottomNavigationRoute(val startDestination: Route) : Route {
        @Serializable
        object FlashcardGraphRoute : BottomNavigationRoute(startDestination = FlashcardRoute)

        @Serializable
        object SmartnoteGraphRoute : BottomNavigationRoute(startDestination = SmartnoteRoute)

        @Serializable
        object ReminderGraphRoute : BottomNavigationRoute(startDestination = ReminderRoute)

        @Serializable
        object SettingsGraphRoute : BottomNavigationRoute(startDestination = SettingsRoute)
    }

    @Serializable
    object SmartnoteRoute : Route

    @Serializable
    object SettingsRoute : Route

    @Serializable
    object FlashcardRoute : Route

    @Serializable
    object CreateFlashcardRoute : Route


    @Serializable
    object CreateSmartnoteRoute : Route

    @Serializable
    object ReminderRoute : Route

    @Serializable
    object CreateReminderRoute : Route

    @Serializable
    data class EditSmartnoteRoute(val id: String) : Route

    companion object {
        val routesShouldShowBottomNavigation = listOf<Route>(
            BottomNavigationRoute.FlashcardGraphRoute,
            BottomNavigationRoute.SmartnoteGraphRoute,
            BottomNavigationRoute.ReminderGraphRoute,
            BottomNavigationRoute.SettingsGraphRoute,
            SmartnoteRoute,
            FlashcardRoute,
            ReminderRoute,
            SettingsRoute
        )
    }
}