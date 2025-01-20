package com.colombo.choresmanager.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
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
import com.colombo.choresmanager.R
import com.colombo.choresmanager.utils.getChoreIntervalOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntervalDropdownField (
    defaultValue: Int,
    onValueChange: (Int) -> Unit = {}
) {
    val context = LocalContext.current

    var intervalDropdownExpanded by remember { mutableStateOf(false) }
    var choreInterval by remember { mutableIntStateOf(defaultValue) }

    ExposedDropdownMenuBox(
        expanded = intervalDropdownExpanded,
        onExpandedChange = {
            intervalDropdownExpanded = it;
            onValueChange(choreInterval)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        TextField(
            value = getChoreIntervalOptions(context).find { it.first == choreInterval }?.second ?: "",
            onValueChange = {  },
            label = { Text(stringResource(R.string.frequency)) },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = intervalDropdownExpanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = intervalDropdownExpanded,
            onDismissRequest = { intervalDropdownExpanded = false },

        ) {
            getChoreIntervalOptions(context).forEach { (value, text) ->
                DropdownMenuItem(
                    text = { Text(text) },
                    onClick = {
                        choreInterval = value
                        intervalDropdownExpanded = false
                        onValueChange(choreInterval)
                    }
                )
            }
        }
    }
}