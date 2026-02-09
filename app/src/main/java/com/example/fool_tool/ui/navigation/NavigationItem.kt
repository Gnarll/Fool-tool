package com.example.fool_tool.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class NavigationItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val route: Route.BottomNavigationRoute,
)