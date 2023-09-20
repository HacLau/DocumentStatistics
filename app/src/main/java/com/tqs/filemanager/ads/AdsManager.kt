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
    var showAdsCount = 0
    var clickAdsCount = 0
    private var adsEntity: AdsEntity? = null
    var adsFullScreen = AdsHelper(AdsItemType.ADSFULLSCREEN)
    var adsInsertResultScan = AdsHelper(AdsItemType.ADSINSERTRESULTSCAN)
    var adsInsertResultClean = AdsHelper(AdsItemType.ADSINSERTRESULTCLEAN)
    var adsNativeMain = AdsHelper(AdsItemType.ADSNATIVEMAIN)
    var adsNativeResultScan = AdsHelper(AdsItemType.ADSNATIVERESULTSCAN)
    var adsNativeResultClean = AdsHelper(AdsItemType.ADSNATIVERESULTCLEAN)

    fun initAdsConfig(adsJson: String) {
        Log.e(TAG, adsJson)
        adsEntity = Gson().fromJson(adsJson, AdsEntity::class.java)
        showAdsCount = adsEntity?.showMax ?: 0
        clickAdsCount = adsEntity?.clickMax ?: 0
        adsFullScreen.initializeSource(adsEntity?.adsOpen)
        adsInsertResultScan.initializeSource(adsEntity?.adsInsertResultScan)
        adsInsertResultClean.initializeSource(adsEntity?.adsInsertResultClean)
        adsNativeMain.initializeSource(adsEntity?.adsNativeMain)
        adsNativeResultScan.initializeSource(adsEntity?.adsNativeResultScan)
        adsNativeResultClean.initializeSource(adsEntity?.adsNativeResultClean)
    }
}