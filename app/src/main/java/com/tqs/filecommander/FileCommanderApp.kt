package com.tqs.filecommander

import android.app.Application
import com.tencent.mmkv.MMKV
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.ads.initReferrer
import com.tqs.filecommander.mmkv.MMKVHelper
import com.tqs.filecommander.tba.TBAHelper
import com.tqs.filecommander.utils.application
import com.tqs.filecommander.utils.getJsonFromAssets

class FileCommanderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val adsJson = getJsonFromAssets(this, "ads.json")
        AdsManager.initAdsConfig(adsJson)
        MMKV.initialize(this)
        initReferrer(this)
        application = this
        if (MMKVHelper.firstLaunchApp) {
            MMKVHelper.firstLaunchApp = false
            TBAHelper.updateInstall()
        }
    }
}