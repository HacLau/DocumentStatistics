package com.tqs.filecommander.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.tqs.filecommander.notification.NotificationHelper
import com.tqs.filecommander.utils.application
import com.tqs.filecommander.utils.logE
import com.tqs.filecommander.utils.toast

class Broadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        "Broadcast ${intent?.action}".toast(application)
        "Broadcast ${intent?.action}".logE()
        when (intent?.action) {
            Intent.ACTION_BATTERY_CHANGED -> {
                NotificationHelper.createNotificationBroadcastBattery(application)
                "BroadcastBattery CONNECTED".logE()
                "BroadcastBattery CONNECTED".toast(application)
            }

            Intent.ACTION_USER_PRESENT -> {
                NotificationHelper.createNotificationBroadcastUnlock(application)
                "BroadcastUnlock UNLOCKED".logE()
                "BroadcastBattery UNLOCKED".toast(application)
            }

            Intent.ACTION_UNINSTALL_PACKAGE -> {
                NotificationHelper.createNotificationBroadcastUninstall(application)
                "BroadcastUninstall UNINSTALL".logE()
                "BroadcastBattery UNINSTALL".toast(application)
            }
        }
    }
}

val broadcast = Broadcast()
 fun registerBattery(context: Context?) {
    context?.registerReceiver(broadcast, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
}

fun registerUnlock(context: Context?){
    context?.registerReceiver(broadcast, IntentFilter(Intent.ACTION_USER_PRESENT))
}

fun registerUninstall(context: Context?){
    context?.registerReceiver(broadcast, IntentFilter(Intent.ACTION_UNINSTALL_PACKAGE))
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
