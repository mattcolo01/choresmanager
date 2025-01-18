package com.colombo.choresmanager.utils

import android.content.Context
import androidx.core.content.ContextCompat.getString
import com.colombo.choresmanager.R

fun getChoreIntervalOptions (context: Context) : List<Pair<Int, String>> {
    return listOf(
        Pair(1, getString(context, R.string.every_day)),
        Pair(2, getString(context, R.string.every_other_day)),
        Pair(3, getString(context, R.string.every_3_days)),
        Pair(7, getString(context, R.string.every_week)),
        Pair(10, getString(context, R.string.every_10_days)),
        Pair(14, getString(context, R.string.every_2_weeks)),
        Pair(21, getString(context, R.string.every_3_weeks)),
        Pair(30, getString(context, R.string.every_month)),
    )
}