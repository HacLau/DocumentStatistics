package com.tqs.filecommander.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.tqs.filecommander.notification.NotificationHelper
import com.tqs.filecommander.tba.EventPoints
import com.tqs.filecommander.tba.TBAHelper
import com.tqs.filecommander.utils.application
import com.tqs.filecommander.utils.logE
import com.tqs.filecommander.utils.toast

val broadcast by lazy {
    object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_BATTERY_CHANGED -> {
                    NotificationHelper.createNotificationBroadcastCharge(application)
                    "BroadcastUninstall Charge".logE()
                }

                Intent.ACTION_USER_PRESENT -> {
                    NotificationHelper.createNotificationBroadcastUnlock(application)
                    "BroadcastUninstall unlock".logE()
                }

                Intent.ACTION_PACKAGE_ADDED -> {
                    NotificationHelper.createNotificationBroadcastUninstall(application)
                    "BroadcastUninstall UNINSTALL".logE()
                    "BroadcastBattery UNINSTALL".toast(application)
                }
            }
        }
    }
}

fun registerBattery(context: Context?) {
    context?.registerReceiver(broadcast, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
}

fun registerUnlock(context: Context?) {
    context?.registerReceiver(broadcast, IntentFilter(Intent.ACTION_USER_PRESENT))
}

fun registerUninstall(context: Context?) {
    context?.registerReceiver(broadcast, IntentFilter(Intent.ACTION_PACKAGE_ADDED).apply {
        addDataScheme("package")
    })
}

fun isBatteryCharging(context: Context?): Boolean {
    return context?.registerReceiver(broadcast, IntentFilter(Intent.ACTION_BATTERY_CHANGED)).let {
        when (it?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)) {
            BatteryManager.BATTERY_PLUGGED_AC, BatteryManager.BATTERY_PLUGGED_USB, BatteryManager.BATTERY_PLUGGED_WIRELESS ->
                true

            else ->
                false
        }
    }
}
