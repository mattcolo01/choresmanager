package com.colombo.choresmanager.view.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.colombo.choresmanager.R
import com.colombo.choresmanager.model.Chore
import com.colombo.choresmanager.view.components.ChoreListItem
import com.colombo.choresmanager.viewmodels.ChoresOverviewViewModel

@Composable
fun ChoresOverviewPage(viewModel: ChoresOverviewViewModel) {
    val choresList by viewModel.choreList.observeAsState()
    var inputText by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .fillMaxHeight()
            .padding(8.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = inputText,
                onValueChange = { inputText = it }
            )

            Button(onClick = {
                viewModel.addChore(inputText)
                inputText = ""
            }) {
                Text(text = "Add")
            }
        }

        choresList?.let {
            LazyColumn (
                content = {
                    itemsIndexed (it) { index, chore ->
                        ChoreListItem(chore = chore, onDelete = { viewModel.deleteChore(chore.id) })
                    }
                }
            )
        }?: Text(
            text = "No chores yet",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}