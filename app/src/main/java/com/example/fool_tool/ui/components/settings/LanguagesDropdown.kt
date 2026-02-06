package com.example.fool_tool.ui.components.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fool_tool.R
import com.example.fool_tool.utils.Language

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguagesDropdown(
    language: Language,
    onSetLanguage: (Language) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = Language.entries

    var isExpanded by remember { mutableStateOf(false) }
    var text by remember { mutableIntStateOf(language.uiName) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        Text(text = stringResource(R.string.lang), style = MaterialTheme.typography.titleMedium)
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it },

            ) {
            TextField(
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                value = stringResource(text),
                readOnly = true,
                singleLine = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                onValueChange = {}
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
            ) {
                Language.entries.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                stringResource(item.uiName),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        onClick = {
                            text = item.uiName
                            onSetLanguage(item)
                            isExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        enabled = item.selectable
                    )
                    if (index != items.size - 1) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun LanguageDropdownPreview() {
    LanguagesDropdown(language = Language.RUSSIAN, onSetLanguage = {})
}