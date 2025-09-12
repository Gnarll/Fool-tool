package com.example.fool_tool.ui.navigation.navigation_bar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.fool_tool.ui.navigation.Route

data class NavigationItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val route: Route.BottomNavigationRoute,
)



