package com.example.fool_tool.ui.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class BottomNavRoute(
) {
    @Serializable
    object FlashcardGraphRoute : BottomNavRoute()

    @Serializable
    object SmartnoteGraphRoute : BottomNavRoute()
}