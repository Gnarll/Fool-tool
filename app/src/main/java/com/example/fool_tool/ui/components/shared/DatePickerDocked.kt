package com.example.fool_tool.ui.components.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Popup
import com.example.fool_tool.R
import com.example.fool_tool.utils.toLocalDateWithZone
import com.example.fool_tool.utils.toUtcLocalDate
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked(onDatePicked: (LocalDate) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis.toUtcLocalDate() >= LocalDate.now()
        }
    })

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDateString(it)
    } ?: ""

    LaunchedEffect(datePickerState) {
        val selectedUtcMillis = datePickerState.selectedDateMillis
        if (selectedUtcMillis != null) {
            onDatePicked(
                selectedUtcMillis.toLocalDateWithZone()
            )

        }
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text(stringResource(R.string.date)) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_calendar_months),
                        contentDescription = stringResource(R.string.select_date)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.text_field_default_height))
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = dimensionResource(R.dimen.text_field_default_height))
                        .shadow(elevation = dimensionResource(R.dimen.elevation_default))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(dimensionResource(R.dimen.padding_large))
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun DatePickerDockedPreview() {
    DatePickerDocked(onDatePicked = {})
}

private fun convertMillisToDateString(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}