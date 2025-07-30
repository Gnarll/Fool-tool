package com.example.fool_tool.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.fool_tool.R


private data class BottomNavigationItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val route: BottomNavigationRoute
)

@Composable
fun BottomNavigationBar(navController: NavHostController, currentRoute: BottomNavigationRoute) {

    val topLevelRoutes: List<BottomNavigationItem> = listOf(
        BottomNavigationItem(
            title = R.string.flashcard_screen_title,
            icon = R.drawable.ic_quiz,
            route = BottomNavigationRoute.FlashcardGraphRoute
        ), BottomNavigationItem(
            title = R.string.smartnote_screen_title,
            icon = R.drawable.ic_notes,
            route = BottomNavigationRoute.SmartnoteGraphRoute
        )
    )

    NavigationBar {
        topLevelRoutes.map { topLevelRoute ->
            NavigationBarItem(
                selected = topLevelRoute.route == currentRoute,
                onClick = {
                    navController.navigate(topLevelRoute.route) {
                        popUpTo(currentRoute::class) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(topLevelRoute.icon),
                        contentDescription = stringResource(topLevelRoute.title)
                    )
                }
            )
        }
    }

}
