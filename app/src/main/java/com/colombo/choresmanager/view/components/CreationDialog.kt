package com.colombo.choresmanager.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CreationDialog(
    onDismiss: () -> Unit,
    onCreate: (String, Int, Long?) -> Unit
) {
    var choreName by remember { mutableStateOf("") }
    var choreInterval by remember { mutableStateOf("") }
    var checked by remember { mutableIntStateOf(0) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                "Create a new chore",
                modifier = Modifier.padding(16.dp)
            )

            OutlinedTextField(
                value = choreName,
                onValueChange = { choreName = it },
                label = { Text("Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            OutlinedTextField(
                value = choreInterval,
                onValueChange = { choreInterval = it },
                label = { Text("Interval") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Did you do it today?"
                )
                Checkbox(
                    checked = checked != 0,
                    onCheckedChange = { checked = if (it) 1 else 0 }
                )
            }

            TextButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = selectedDate?.let { "Selected Date: $it" } ?: "Select Date")
            }

            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextButton(onClick = onDismiss ) {
                    Text(text = "Cancel")
                }
                TextButton(onClick = { onCreate(choreName, Integer.valueOf(choreInterval), selectedDate) }) {
                    Text(text = "Create")
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { date ->
                selectedDate = date
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}