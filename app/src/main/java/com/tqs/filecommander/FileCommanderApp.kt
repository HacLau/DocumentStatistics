package com.tqs.filecommander

import android.app.Application
import android.content.Intent
import android.os.Build
import com.tencent.mmkv.MMKV
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.broadcast.registerBattery
import com.tqs.filecommander.broadcast.registerUninstall
import com.tqs.filecommander.broadcast.registerUnlock
import com.tqs.filecommander.notification.NotificationController
import com.tqs.filecommander.referrer.ReferrerHelper
import com.tqs.filecommander.service.ForeService
import com.tqs.filecommander.utils.application
import com.tqs.filecommander.utils.getJsonFromAssets
import com.tqs.filecommander.utils.logE

class FileCommanderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val adsJson = getJsonFromAssets(this, "ads.json")
        AdsManager.initAdsConfig(adsJson)
        MMKV.initialize(this)
        NotificationController.initNotificationConfig(this)
        ReferrerHelper.initReferrer(this)
        application = this
//        startService()
        registerBroadcast()
    }

    private fun registerBroadcast() {
        registerBattery(this)
        registerUnlock(this)
        registerUninstall(this)
    }

    private fun startService(){
        Intent(this, ForeService::class.java).apply {
//            putExtra("","")
        }.let {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
                startForegroundService(it)
            }else{
                startService(it)
            }
        }

    }
}