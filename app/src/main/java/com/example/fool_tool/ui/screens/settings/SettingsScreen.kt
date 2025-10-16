package com.example.fool_tool.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fool_tool.R
import com.example.fool_tool.ui.components.settings.LanguagesDropdown
import com.example.fool_tool.ui.components.settings.ThemeRadioGroup


@Composable
fun SettingsScreen(modifier: Modifier = Modifier, viewModel: SettingsViewModel = hiltViewModel()) {
    val theme = viewModel.theme.collectAsStateWithLifecycle().value
    val language = viewModel.language.collectAsStateWithLifecycle().value

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_x_x_large)),
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .verticalScroll(rememberScrollState())
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = dimensionResource(R.dimen.surface_shadow_elevation),
                tonalElevation = dimensionResource(R.dimen.surface_tonal_elevation),
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                ThemeRadioGroup(
                    selectedTheme = theme,
                    onSelectTheme = viewModel::setTheme,
                    Modifier.padding(dimensionResource(R.dimen.padding_small))
                )
            }
            LanguagesDropdown(
                language = language,
                onSetLanguage = viewModel::setLanguage,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )
        }
    }
}