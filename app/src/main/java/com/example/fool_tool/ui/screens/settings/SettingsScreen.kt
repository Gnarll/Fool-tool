package com.example.fool_tool.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fool_tool.ui.components.settings.ThemeRadioGroup


@Composable
fun SettingsScreen(modifier: Modifier = Modifier, viewModel: SettingsViewModel = hiltViewModel()) {
    val theme = viewModel.theme.collectAsStateWithLifecycle()
    
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        ThemeRadioGroup(
            selectedTheme = theme.value,
            onSelectTheme = { theme -> viewModel.setTheme(theme) })
    }
}