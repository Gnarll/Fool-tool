package com.example.fool_tool.ui.navigation.navigation_bar

import kotlinx.serialization.Serializable

@Serializable
sealed class BottomNavigationRoute(
) {
    @Serializable
    object FlashcardGraphRoute : BottomNavigationRoute()

    @Serializable
    object SmartnoteGraphRoute : BottomNavigationRoute()


    companion object {
        val items = listOf(
            FlashcardGraphRoute,
            SmartnoteGraphRoute
        )
    }
}