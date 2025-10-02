package com.example.fool_tool.ui.components.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.fool_tool.R
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialTimePicker(
    onTimePicked: (LocalTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentTime = LocalTime.now()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.hour,
        initialMinute = currentTime.minute,
        is24Hour = true,
    )

    var showTimePicker by remember { mutableStateOf(false) }

    val dismiss = { showTimePicker = false }
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = convertToTimeString(timePickerState.hour, timePickerState.minute),
            onValueChange = { },
            label = { Text(stringResource(R.string.time)) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showTimePicker = !showTimePicker }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_clock),
                        contentDescription = stringResource(R.string.select_time)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.text_field_default_height))
        )

        if (showTimePicker) {
            PickerDialog(
                onConfirm = {
                    onTimePicked(LocalTime.of(timePickerState.hour, timePickerState.minute))
                    dismiss()
                },
                onDismiss = {
                    dismiss()
                })
            {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    TimePicker(
                        state = timePickerState,
                    )

                }
            }
        }
    }


}

private fun convertToTimeString(hours: Int, minutes: Int): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(Date(((hours * 3600 + minutes * 60).toLong() * 1000)))
}