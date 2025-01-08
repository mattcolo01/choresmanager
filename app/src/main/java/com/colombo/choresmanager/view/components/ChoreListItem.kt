package com.colombo.choresmanager.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.colombo.choresmanager.R
import com.colombo.choresmanager.model.Chore
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ChoreListItem(
    chore: Chore,
    onDelete: () -> Unit,
    onComplete: () -> Unit
) {
    ElevatedCard (
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row (
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = chore.name,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                LinearProgressIndicator(
                    progress = {
                        1f - (
                                Duration.between(
                                    chore.lastDoneAt,
                                    LocalDateTime.now()
                                ).seconds.toFloat() / Duration.ofDays(chore.intervalDays.toLong()).seconds.toFloat()
                        ).coerceIn(0f, 1f)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                Text(
                    text = "Days: ${chore.intervalDays}",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.surfaceTint
                )
                Text(
                    text = "Last: ${chore.lastDoneAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.surfaceTint
                )
            }

            IconButton(onClick = onComplete) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_done_24),
                    contentDescription = "Complete",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_delete_24),
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}