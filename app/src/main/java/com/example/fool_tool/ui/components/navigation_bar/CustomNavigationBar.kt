package com.example.fool_tool.ui.components.navigation_bar

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import com.example.fool_tool.R
import com.example.fool_tool.ui.navigation.NavigationItem
import com.example.fool_tool.ui.navigation.Route


@Composable
fun CustomNavigationBar(
    navigationItems: List<NavigationItem>,
    currentNavRoute: Route.BottomNavigationRoute,
    navigateTo: (Route.BottomNavigationRoute) -> Unit
) {
    val selectedItemIndex =
        navigationItems.indexOfFirst { currentNavRoute::class == it.route::class }

    val cs = MaterialTheme.colorScheme
    val indicatorColor = cs.secondaryContainer
    val backgroundColor = cs.surfaceContainer
    val navBarItemColor = cs.onSurfaceVariant

    CustomNavigationBarLayout(
        innerPadding = PaddingValues(
            top = dimensionResource(R.dimen.padding_medium),
            start = dimensionResource(R.dimen.padding_x_small),
            end = dimensionResource(R.dimen.padding_medium),
        ),
        selectedIndex = selectedItemIndex,
        floatingNavigationIndicator = { shapeProgressProvider ->
            FloatingNavigationIndicator(
                color = indicatorColor,
                shapeProgressProvider = shapeProgressProvider,
                modifier = Modifier
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
                    navigateTo(navItem.route)
                },
                iconVector = ImageVector.vectorResource(id = navItem.icon),
                labelText = stringResource(navItem.title),
                color = if (isSelected) navBarItemColor else navBarItemColor.copy(alpha = 0.5f)
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
    iconVector: ImageVector,
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
            imageVector = iconVector,
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