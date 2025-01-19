package com.colombo.choresmanager.view.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.colombo.choresmanager.R
import com.colombo.choresmanager.model.Chore
import com.colombo.choresmanager.utils.getChoreIntervalOptions
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ChoreListItem(
    chore: Chore,
    onDelete: () -> Unit,
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    val cardDefaultBackground = MaterialTheme.colorScheme.surfaceContainerHigh
    val defaultBarColor = MaterialTheme.colorScheme.onSurface

    val timeSinceLastCompletion = Duration.between(
        chore.lastDoneAt,
        ZonedDateTime.now()
    )
    val timeBetweenCompletions = Duration.ofDays(chore.intervalDays.toLong())
    val animateBackground = (timeBetweenCompletions.seconds - timeSinceLastCompletion.seconds) <= 0
    val animateBar = (timeBetweenCompletions.seconds - timeSinceLastCompletion.seconds) in 0..(3600 * 24)

    var isFlashing by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "backgroundFlashing")

    if (animateBackground || animateBar) {
        LaunchedEffect(Unit) {
            isFlashing = true
        }
    }

    val backgroundColor by
        if (animateBackground)
            infiniteTransition.animateColor(
                initialValue = cardDefaultBackground,
                targetValue = if (isFlashing) Color.Red else cardDefaultBackground,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000),
                    repeatMode = RepeatMode.Reverse
                ), label = "colorAnimation"
            )
        else
            remember { mutableStateOf(cardDefaultBackground) }

    val barColor by
        if (animateBar)
            infiniteTransition.animateColor(
                initialValue = defaultBarColor,
                targetValue = if (isFlashing) Color.Red else defaultBarColor,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000),
                    repeatMode = RepeatMode.Reverse
                ), label = "colorAnimation"
            )
        else
            remember { mutableStateOf(defaultBarColor) }

    ElevatedCard (
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
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
                                timeSinceLastCompletion.seconds.toFloat() / timeBetweenCompletions.seconds.toFloat()
                        ).coerceIn(0f, 1f)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    trackColor = MaterialTheme.colorScheme.surface,
                    color = barColor,
                    drawStopIndicator = { },
                    strokeCap = StrokeCap.Round,
                    gapSize = 4.dp
                )
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = getChoreIntervalOptions(context).find { it.first == chore.intervalDays }?.second ?: "",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.surfaceTint
                    )
                    Text(
                        text = stringResource(R.string.done_at) + chore.lastDoneAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.surfaceTint
                    )
                }
            }

            IconButton(onClick = onComplete) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_done_24),
                    contentDescription = stringResource(R.string.complete),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_delete_24),
                    contentDescription = stringResource(R.string.delete),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}