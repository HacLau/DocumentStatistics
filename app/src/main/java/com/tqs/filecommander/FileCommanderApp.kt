package com.tqs.filecommander

import android.app.Application
import android.content.Intent
import android.os.Build
import com.blankj.utilcode.util.TimeUtils
import com.tencent.mmkv.MMKV
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.broadcast.registerBattery
import com.tqs.filecommander.broadcast.registerUninstall
import com.tqs.filecommander.broadcast.registerUnlock
import com.tqs.filecommander.mmkv.MMKVHelper
import com.tqs.filecommander.notification.NotificationController
import com.tqs.filecommander.notification.NotificationHelper
import com.tqs.filecommander.referrer.ReferrerHelper
import com.tqs.filecommander.service.ForeService
import com.tqs.filecommander.tba.EventPoints
import com.tqs.filecommander.tba.TBAHelper
import com.tqs.filecommander.utils.DateUtils
import com.tqs.filecommander.utils.application
import com.tqs.filecommander.utils.getJsonFromAssets
import com.tqs.filecommander.utils.logE
import java.util.Timer
import java.util.TimerTask

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
        timerPoints()
        timerNotification()
        if (0L == MMKVHelper.firstLaunchAppTime) {
            MMKVHelper.firstLaunchAppTime = System.currentTimeMillis()
        }
        if (TimeUtils.isToday(MMKVHelper.launchAppTime).not()){
            TBAHelper.updatePoints(EventPoints.filec_retention, mutableMapOf(
                EventPoints.D to "D${DateUtils.getMillisDay(MMKVHelper.firstLaunchAppTime) - DateUtils.getMillisDay(MMKVHelper.firstLaunchAppTime)}"))
        }
        MMKVHelper.launchAppTime = System.currentTimeMillis()

    }

    private fun timerNotification() {
        Timer().schedule(object : TimerTask(){
            override fun run() {
                NotificationHelper.createNotificationScheduled(this@FileCommanderApp)
            }
        },0,1000 * 60 * 10)
    }

    private fun timerPoints() {
        Timer().schedule(object : TimerTask(){
            override fun run() {
                TBAHelper.updatePoints(EventPoints.filec_session_back)
            }
        },0,30000)

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