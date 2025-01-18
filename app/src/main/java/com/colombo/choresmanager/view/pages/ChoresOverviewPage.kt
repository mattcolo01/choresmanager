package com.colombo.choresmanager.view.pages

import AdListItem
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.colombo.choresmanager.R
import com.colombo.choresmanager.view.components.ChoreListItem
import com.colombo.choresmanager.view.components.FloatingAddButton
import com.colombo.choresmanager.view.components.dialogs.CreationDialog
import com.colombo.choresmanager.view.components.dialogs.DeletionConfirmDialog
import com.colombo.choresmanager.viewmodels.ChoresOverviewViewModel
import kotlinx.coroutines.launch

@Composable
fun ChoresOverviewPage(
    viewModel: ChoresOverviewViewModel,
    showInterstitialAd: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val choresList by viewModel.choreList.observeAsState()
    val openCreationDialog = remember { mutableStateOf(false) }
    val choreFlaggedForDeletion = remember { mutableIntStateOf(-1) }

    Scaffold (
        floatingActionButton = { FloatingAddButton(onClick = {
            openCreationDialog.value = true
        }) },
        floatingActionButtonPosition = FabPosition.End,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar (
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                    shape = RoundedCornerShape(16.dp),
                    snackbarData = data,
                    modifier = Modifier
                        .zIndex(Float.POSITIVE_INFINITY)
                )
            }
       },
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
                            if (index % 7 == 3) {
                                AdListItem()
                            }
                            ChoreListItem(
                                chore = chore,
                                onDelete = { choreFlaggedForDeletion.intValue = chore.id },
                                onComplete = { viewModel.completeChore(chore.id) }
                            )
                        }

                        if (it.isEmpty()) {
                            item {
                                Text(
                                    text = stringResource(R.string.no_chores),
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                )
                            }
                        }
                    }
                )
            }
        }



        /* *** Dialogs *** */

        when {
            openCreationDialog.value -> {
                CreationDialog (
                    onDismiss = { openCreationDialog.value = false },
                    onCreate = { name, interval, date ->
                        openCreationDialog.value = false
                        showInterstitialAd()
                        viewModel.addChore(name, interval, date)
                    },
                    onError = { message ->
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = message,
                                actionLabel = null,
                                duration = SnackbarDuration.Short,
                                withDismissAction = true,
                            )
                        }
                    }
                )
            }
            choreFlaggedForDeletion.intValue != -1 -> {
                DeletionConfirmDialog(
                    onDismissRequest = { choreFlaggedForDeletion.intValue = -1 },
                    onConfirmation = {
                        viewModel.deleteChore(choreFlaggedForDeletion.intValue)
                        choreFlaggedForDeletion.intValue = -1
                    }
                )
            }
        }
    }
}

