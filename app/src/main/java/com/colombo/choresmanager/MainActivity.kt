package com.colombo.choresmanager

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.colombo.choresmanager.ui.theme.ChoresManagerTheme
import com.colombo.choresmanager.view.pages.ChoresOverviewPage
import com.colombo.choresmanager.viewmodels.ChoresOverviewViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MainActivity : ComponentActivity() {

    private var mInterstitialAd: InterstitialAd? = null
    private val tag = "MainApplication"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val choresOverviewViewModel = ViewModelProvider(this)[ChoresOverviewViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            ChoresManagerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChoresOverviewPage(choresOverviewViewModel) { showInterstitialAd() }
                }
            }
        }

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-5545966595390113/2444977770", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(tag, adError.toString())
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(tag, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })
    }

    private fun showInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        } else {
            Log.d(tag, "The interstitial ad wasn't ready yet.")
        }
    }
}
