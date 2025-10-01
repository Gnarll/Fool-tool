package com.example.fool_tool.ui.navigation.navigation_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import com.example.fool_tool.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@Composable
fun CustomNavigationBar(
    selectedItemIndex: Int,
    navigationItems: List<NavigationItem>,
    navigateTo: (NavigationItem) -> Unit
) {
    val indicatorAnimationManager = rememberIndicatorAnimationManager()

    val coroutineScope = rememberCoroutineScope()
    var currentAnimationJob: Job? by remember { mutableStateOf(null) }

    val navItemsCoordinates = remember { mutableStateMapOf<Int, Offset>() }
    var indicatorCoordinates: Offset by remember { mutableStateOf(Offset.Zero) }

    val cs = MaterialTheme.colorScheme
    val indicatorColor = cs.secondaryContainer
    val backgroundColor = cs.surfaceContainer
    val navBarItemColor = cs.onSurfaceVariant

    LaunchedEffect(Unit) {
        val targetX = calculateRelativeOffsetFromIndicator(
            indicatorCoordinates,
            navItemsCoordinates,
            selectedItemIndex
        ).x

        currentAnimationJob = coroutineScope.launch {
            indicatorAnimationManager.animateTo(targetX)
        }
    }


    CustomNavigationBarLayout(
        innerPadding = PaddingValues(
            top = dimensionResource(R.dimen.padding_medium),
            start = dimensionResource(R.dimen.padding_x_small),
            end = dimensionResource(R.dimen.padding_medium),
        ),
        floatingNavigationIndicator = {
            FloatingNavigationIndicator(
                color = indicatorColor,
                shapeProgress = indicatorAnimationManager.shapeAnimation.value,
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        indicatorCoordinates = coordinates.positionInParent()
                    }
                    .graphicsLayer {
                        this.translationX = indicatorAnimationManager.offsetAnimation.value
                    }

            )
        },
        navigationBarBackground = {
            NavigationBarBackground(
                modifier = Modifier.background(color = backgroundColor)
            )
        },
    ) {
        navigationItems.forEachIndexed { navItemIndex, navItem ->
            val isSelected = navItemIndex == selectedItemIndex

            CustomNavigationBarItem(
                onClick = {
                    navigateTo(navItem)


                    val targetX = calculateRelativeOffsetFromIndicator(
                        indicatorCoordinates,
                        navItemsCoordinates,
                        navItemIndex
                    ).x

                    currentAnimationJob?.cancel()
                    currentAnimationJob = coroutineScope.launch {
                        indicatorAnimationManager.animateTo(targetX)
                    }
                },
                icon = painterResource(navItem.icon),
                labelText = stringResource(navItem.title),
                color = if (isSelected) navBarItemColor else navBarItemColor.copy(alpha = 0.5f),
                modifier = Modifier.onGloballyPositioned { coordinates ->
                    navItemsCoordinates[navItemIndex] = coordinates.positionInParent()
                }
            )
        }
    }
}


@Composable
fun NavigationBarBackground(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier)
}


@Composable
fun CustomNavigationBarItem(
    onClick: () -> Unit,
    icon: Painter,
    labelText: String,
    color: Color,
    modifier: Modifier = Modifier,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.padding_x_small)
        ),
        modifier = modifier
            .clickable(
                onClick = onClick, role = Role.Tab,
                indication = null,
                interactionSource = null,
            )
    ) {
        Icon(
            painter = icon,
            contentDescription = labelText,
            tint = color,
            modifier = Modifier.size(dimensionResource(R.dimen.nav_icon_size))
        )
        Text(
            text = labelText,
            style = MaterialTheme.typography.labelLarge,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}