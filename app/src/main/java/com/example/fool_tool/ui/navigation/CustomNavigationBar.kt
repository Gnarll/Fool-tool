package com.example.fool_tool.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.util.lerp
import androidx.navigation.NavHostController
import com.example.fool_tool.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


data class BottomNavigationItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val route: BottomNavigationRoute
)

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


sealed interface IndicatorState {
    object Idle : IndicatorState
    object Shrinking : IndicatorState
    object Moving : IndicatorState
    object Stretching : IndicatorState
}


@Composable
fun CustomNavigationBar(navController: NavHostController, currentRoute: BottomNavigationRoute) {
    val coroutineScope = rememberCoroutineScope()

    var navigationItemWidth by remember {
        mutableIntStateOf(0)
    }
    var barHeight by remember { mutableIntStateOf(0) }

    var indicatorState: IndicatorState by remember { mutableStateOf(IndicatorState.Idle) }
    var currentIndicatorJob: Job? by remember { mutableStateOf(null) }

    val indicatorShapeMorphProgress = remember { Animatable(1f) }
    val indicatorOffsetXProgress =
        remember { Animatable(1f / topLevelRoutes.size) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.padding_x_small))
    ) {
        Box(
            modifier = Modifier
                .size(with(LocalDensity.current) { barHeight.toDp() })
                .background(color = Color.Blue)
                .graphicsLayer {
                    val translationX = lerp(
                        0f,
                        navigationItemWidth.toFloat() * topLevelRoutes.size.toFloat(),
                        indicatorOffsetXProgress.value
                    )

                    this.translationX = translationX
                },
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onPlaced { coordinates ->
                    barHeight = coordinates.size.height
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_x_small))

        ) {
            topLevelRoutes.mapIndexed { routeIndex, route ->
                val isSelected = remember(currentRoute) {
                    currentRoute == route.route
                }

                val itemColor by
                animateColorAsState(
                    targetValue = if (isSelected) Color.Black.copy(alpha = 0.8f) else Color.Black.copy(
                        alpha = 0.4f
                    )
                )


                CustomNavigationBarItem(
                    onClick = {
                        currentIndicatorJob?.cancel()
                        currentIndicatorJob = coroutineScope.launch {

                            val targetOffsetX =
                                (routeIndex + 1).toFloat() / (topLevelRoutes.size).toFloat()
                            when (indicatorState) {
                                is IndicatorState.Idle,
                                is IndicatorState.Shrinking,
                                IndicatorState.Stretching -> {
                                    indicatorState = IndicatorState.Shrinking
                                    indicatorShapeMorphProgress.animateTo(
                                        targetValue = 0f,
                                    )

                                    indicatorState = IndicatorState.Moving
                                    indicatorOffsetXProgress.animateTo(
                                        targetOffsetX,
                                    )

                                    indicatorState = IndicatorState.Stretching
                                    indicatorShapeMorphProgress.animateTo(
                                        targetValue = 1f,
                                    )
                                }

                                is IndicatorState.Moving -> {
                                    indicatorOffsetXProgress.animateTo(
                                        targetOffsetX,
                                    )

                                    indicatorState = IndicatorState.Stretching
                                    indicatorShapeMorphProgress.animateTo(
                                        targetValue = 1f,
                                    )
                                }

                            }
                        }

                        navController.navigate(route.route) {
                            popUpTo(currentRoute::class) {
                                saveState = true
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(route.icon),
                            contentDescription = stringResource(route.title),
                            tint = itemColor,
                            modifier = Modifier.size(dimensionResource(R.dimen.nav_icon_size))
                        )
                    },
                    label = { Text(text = stringResource(route.title), color = itemColor) },
                    modifier = Modifier
                        .weight(1f)
                        .onPlaced { coordinates ->
                            if (navigationItemWidth == 0) {
                                navigationItemWidth = coordinates.size.width
                            }
                        }
                )
            }

        }

    }

}


@Composable
fun CustomNavigationBarItem(
    onClick: () -> Unit,
    icon: @Composable (() -> Unit),
    label: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.padding_x_small)
        ),
        modifier = modifier.clickable(
            onClick = onClick, role = Role.Tab,
            indication = null,
            interactionSource = null,
        )
    ) {
        icon()
        label()
    }
}