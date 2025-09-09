package com.example.fool_tool.ui.components.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fool_tool.R

enum class Theme(@StringRes val title: Int) {
    DARK_THEME(R.string.dark_theme),
    LIGHT_THEME(R.string.light_theme),
    SYSTEM_THEME(R.string.system_theme)
}

@Composable
fun ThemeRadioGroup(
    selectedTheme: Theme,
    onSelectTheme: (Theme) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        Text(
            text = stringResource(R.string.theme_settings),
            style = MaterialTheme.typography.titleMedium
        )
        Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))) {
            Theme.entries.map { themeRadioButton ->
                val selected = selectedTheme == themeRadioButton

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selected, onClick = { onSelectTheme(themeRadioButton) })
                    Spacer(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_x_small)))
                    Text(
                        text = stringResource(themeRadioButton.title),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ThemeRadioGroupPreview() {
    ThemeRadioGroup(selectedTheme = Theme.DARK_THEME, onSelectTheme = {})
}