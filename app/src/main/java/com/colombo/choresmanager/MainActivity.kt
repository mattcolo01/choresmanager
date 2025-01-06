package com.colombo.choresmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.colombo.choresmanager.view.pages.ChoresOverviewPage
import com.colombo.choresmanager.ui.theme.ChoresManagerTheme
import com.colombo.choresmanager.viewmodels.ChoresOverviewViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val choresOverviewViewModel = ViewModelProvider(this).get(ChoresOverviewViewModel::class.java)
        enableEdgeToEdge()
        setContent {
            ChoresManagerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChoresOverviewPage(choresOverviewViewModel)
                }
            }
        }
    }
}
