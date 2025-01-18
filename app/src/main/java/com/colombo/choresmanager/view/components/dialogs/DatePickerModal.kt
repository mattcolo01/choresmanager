package com.colombo.choresmanager.view.components.dialogs

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.colombo.choresmanager.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(localTimeStampToUTC(datePickerState.selectedDateMillis!!))
                onDismiss()
            }) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    text = stringResource(R.string.when_last_done),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(22.dp)
                )
            }
        )
    }
}

fun localTimeStampToUTC(timestampMs: Long): Long {
    val localDateTime = LocalDateTime.ofEpochSecond(timestampMs / 1000, 0, ZoneOffset.UTC)
    val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault())
    return zonedDateTime.toEpochSecond()
}