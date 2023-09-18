package com.tqs.filemanager.ads

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.gson.Gson
import com.tqs.document.statistics.BuildConfig


object AdsManager {
    const val TAG = "AdsManager"
    private var showMax = 0
    private var clickMax = 0
    private var adsEntity: AdsEntity? = null
    var adsOpen = AdsHelper(AdsType.ADSOPEN)
    var adsInsertResultScan = AdsHelper(AdsType.ADSINSERTRESULTSCAN)
    var adsInsertResultClean = AdsHelper(AdsType.ADSINSERTRESULTCLEAN)
    var adsNativeMain = AdsHelper(AdsType.ADSNATIVEMAIN)
    var adsNativeResultScan = AdsHelper(AdsType.ADSNATIVERESULTSCAN)
    var adsNativeResultClean = AdsHelper(AdsType.ADSNATIVERESULTCLEAN)

    fun initAdsConfig(adsJson: String) {
        Log.e(TAG, adsJson)
        adsEntity = Gson().fromJson(adsJson, AdsEntity::class.java)
        showMax = adsEntity?.showMax ?: 0
        clickMax = adsEntity?.clickMax ?: 0
        adsOpen.initializeSource(adsEntity?.adsOpen)
        adsInsertResultScan.initializeSource(adsEntity?.adsInsertResultScan)
        adsInsertResultClean.initializeSource(adsEntity?.adsInsertResultClean)
        adsNativeMain.initializeSource(adsEntity?.adsNativeMain)
        adsNativeResultScan.initializeSource(adsEntity?.adsNativeResultScan)
        adsNativeResultClean.initializeSource(adsEntity?.adsNativeResultClean)
    }
}