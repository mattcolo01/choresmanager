package com.colombo.choresmanager

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.colombo.choresmanager.receivers.IntentReceiver
import com.colombo.choresmanager.ui.theme.ChoresManagerTheme
import com.colombo.choresmanager.utils.DEFAULT_CHANNEL_ID
import com.colombo.choresmanager.view.pages.ChoresOverviewPage
import com.colombo.choresmanager.viewmodels.ChoresOverviewViewModel
import com.colombo.choresmanager.viewmodels.ViewModelFactory
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.util.Calendar

class MainActivity : ComponentActivity() {

    private var mInterstitialAd: InterstitialAd? = null
    private val tag = "MainActivity"
    private var choresOverviewViewModel: ChoresOverviewViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        choresOverviewViewModel = ViewModelProvider(this, ViewModelFactory())[ChoresOverviewViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            ChoresManagerTheme {
                    ChoresOverviewPage(
                        choresOverviewViewModel!!,
                        this::showInterstitialAd,
                        this::scheduleNotificationForChore
                    )
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            askForNotificationPermission()
        }
        createNotificationChannel()

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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun askForNotificationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(DEFAULT_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun scheduleNotificationForChore(choreId: MutableLiveData<Int>) {
        choreId.observe(this) { id ->
            choreId.removeObservers(this)
            val sub = choresOverviewViewModel!!.getChore(id)
            sub.observe(this) {
                sub.removeObservers(this)

                val intent = Intent(this, IntentReceiver::class.java)
                intent.putExtra("choreId", id)
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    id,
                    intent,
                    PendingIntent.FLAG_MUTABLE
                )

                val alarmManager = this.getSystemService(ALARM_SERVICE) as AlarmManager
                val selectedDate = Calendar.getInstance().apply {
                    timeInMillis =
                        it.lastDoneAt.plusDays(it.intervalDays.toLong()).toInstant().toEpochMilli()
                }

                val year = selectedDate.get(Calendar.YEAR)
                val month = selectedDate.get(Calendar.MONTH)
                val day = selectedDate.get(Calendar.DAY_OF_MONTH)

                val calendar = Calendar.getInstance()
                calendar.set(year, month, day, 8, 0)

                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        }
    }
}