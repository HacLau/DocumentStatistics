package com.tqs.filemanager

import android.app.Application
import com.tqs.filemanager.ads.AdsManager
import com.tqs.filemanager.vm.utils.*

class FileCommanderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val adsJson = getJsonFromAssets(this, "ads.json")
        AdsManager.initAdsConfig(adsJson)
    }
}