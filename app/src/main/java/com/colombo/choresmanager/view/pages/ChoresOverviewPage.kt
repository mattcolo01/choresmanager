package com.colombo.choresmanager.view.pages

import AdListItem
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.colombo.choresmanager.view.components.ChoreListItem
import com.colombo.choresmanager.view.components.CreationDialog
import com.colombo.choresmanager.view.components.FloatingAddButton
import com.colombo.choresmanager.viewmodels.ChoresOverviewViewModel

@Composable
fun ChoresOverviewPage(viewModel: ChoresOverviewViewModel) {
    val choresList by viewModel.choreList.observeAsState()
    val openCreationDialog = remember { mutableStateOf(false) }

    Scaffold (
        floatingActionButton = { FloatingAddButton(onClick = {
            openCreationDialog.value = true
        }) },
        floatingActionButtonPosition = FabPosition.End
    ) { innerpadding ->
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(innerpadding)
                .padding(16.dp),
        ) {
            choresList?.sortedBy { it.lastDoneAt.plusDays(it.intervalDays.toLong()) }?.let {
                LazyColumn(
                    content = {
                        itemsIndexed(it) { index, chore ->
                            if (index % 10 == 3) {
                                AdListItem()
                            }
                            ChoreListItem(
                                chore = chore,
                                onDelete = { viewModel.deleteChore(chore.id) },
                                onComplete = { viewModel.completeChore(chore.id) }
                            )
                        }
                    }
                )
            } ?: Text(
                text = "No chores yet",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }

    /* *** Dialogs *** */

    when {
        openCreationDialog.value -> {
            CreationDialog (
                onDismiss = { openCreationDialog.value = false },
                onCreate = { name, interval, _ ->
                    viewModel.addChore(name, interval)
                    openCreationDialog.value = false
                },
            )
        }
    }
}

