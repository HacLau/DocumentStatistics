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
    private var appOpenAd: AppOpenAd? = null
    private var mInterstitialAd: InterstitialAd? = null
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

    fun loadOpenAds(activity: Activity) {
        val request = AdRequest.Builder().build()
        AppOpenAd.load(activity, BuildConfig.AD_UNIT_ID, request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, object : AppOpenAdLoadCallback() {
            override fun onAdLoaded(ad: AppOpenAd) {
                appOpenAd = ad
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            }
        })
    }

    fun loadInterAds(activity: Activity) {
        val build = AdRequest.Builder().build()
        InterstitialAd.load(activity, BuildConfig.AD_UNIT_ID, build, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                mInterstitialAd = ad
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {

            }
        })
    }

    fun showOpenAds(activity: Activity) {
        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                // Called when full screen content is dismissed.
                // Set the reference to null so isAdAvailable() returns false.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                appOpenAd = null
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                // Called when fullscreen content failed to show.
                // Set the reference to null so isAdAvailable() returns false.
                Log.d(TAG, adError.message)
                appOpenAd = null
            }

            override fun onAdShowedFullScreenContent() {
                // Called when fullscreen content is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }

        if (appOpenAd != null) {
            appOpenAd?.show(activity)
        } else {
            Log.d("TAG", "The appOpenAd ad wasn't ready yet.")
        }
    }

    fun showInterAds(activity: Activity) {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(activity)
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }
}