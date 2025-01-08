package com.colombo.choresmanager.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CreationDialog(
    onDismiss: () -> Unit,
    onCreate: (String, Int) -> Unit
) {
    var choreName by remember { mutableStateOf("") }
    var choreInterval by remember { mutableIntStateOf(1) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            OutlinedTextField(
                value = choreName,
                onValueChange = { choreName = it },
                label = { Text("Label") }
            )

            OutlinedTextField(
                value = choreInterval.toString(),
                onValueChange = { choreInterval = Integer.valueOf(it) }, //correggere
                label = { Text("Label") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onDismiss ) {
                    Text(text = "Cancel")
                }
                TextButton(onClick = { onCreate(choreName, choreInterval) }) {
                    Text(text = "Create")
                }
            }
        }
    }
}