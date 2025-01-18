package com.colombo.choresmanager.view.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat.getString
import com.colombo.choresmanager.R
import com.colombo.choresmanager.view.components.IntervalDropdownField

@Composable
fun CreationDialog(
    onDismiss: () -> Unit,
    onCreate: (String, Int, Long) -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current

    var choreName by remember { mutableStateOf("") }
    var choreInterval by remember { mutableIntStateOf(7) }
    var showDatePicker by remember { mutableStateOf(false) }
    var choreNameTouched by remember { mutableStateOf(false) }


    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                stringResource(R.string.create_chore),
                modifier = Modifier
                    .padding(16.dp),
                fontSize = 20.sp
            )

            TextField(
                value = choreName,
                onValueChange = { choreName = it; choreNameTouched = true },
                label = { Text(stringResource(R.string.name)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                isError = choreName.isEmpty() && choreNameTouched
            )

            IntervalDropdownField(
                defaultValue = choreInterval,
                onValueChange = { choreInterval = it }
            )

            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextButton(onClick = onDismiss ) {
                    Text(text = stringResource(R.string.cancel))
                }
                TextButton(
                    onClick = {
                        if (choreName.isEmpty()) {
                            onError(getString(context, R.string.required_fields))
                            return@TextButton
                        } else {
                            showDatePicker = true
                        }
                    },
                ) {
                    Text(stringResource(R.string.create))
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { date ->
                if (date == null) {
                    onError(getString(context, R.string.required_date))
                    return@DatePickerModal
                } else {
                    showDatePicker = false
                    onCreate(choreName, Integer.valueOf(choreInterval), date)
                }
            },
            onDismiss = { showDatePicker = false }
        )
    }
}