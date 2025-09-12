package com.example.fool_tool.ui.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed interface Route {
    @Serializable
    sealed interface BottomNavigationRoute : Route {
        @Serializable
        object FlashcardGraphRoute : BottomNavigationRoute

        @Serializable
        object SmartnoteGraphRoute : BottomNavigationRoute

        @Serializable
        object SettingsRoute : BottomNavigationRoute
    }

    @Serializable
    object FlashcardRoute : Route

    @Serializable
    object CreateFlashcardRoute : Route

    @Serializable
    object SmartnoteRoute : Route

    @Serializable
    object CreateSmartnoteRoute : Route

    @Serializable
    data class EditSmartnoteRoute(val id: String) : Route

    companion object {
        val routesShouldShowBottomNavigation = listOf<Route>(
            BottomNavigationRoute.FlashcardGraphRoute,
            BottomNavigationRoute.SmartnoteGraphRoute,
            BottomNavigationRoute.SettingsRoute,
            FlashcardRoute,
            SmartnoteRoute
        )
    }
}