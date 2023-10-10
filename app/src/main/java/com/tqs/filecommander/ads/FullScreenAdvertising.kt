package com.tqs.filecommander.ads

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.tqs.filecommander.base.BaseAds
import com.tqs.filecommander.utils.logE

class FullScreenAdvertising(
    private val context: Context,
    private val adsType: AdsItemType,
    private val item: AdsItem
) : BaseAds(adsType, item) {
    private val adsRequest: AdRequest
        get() = AdRequest.Builder().build()
    private var ad: Any? = null
    override fun load(onAdsLoaded: () -> Unit, onAdsLoadFailed: (msg: String?) -> Unit) {
        "AdsOpen load".logE()
        when (item.adsType) {
            ADSType.ADS_TYPE_OP -> loadOpenAdvertising(onAdsLoaded, onAdsLoadFailed)
            ADSType.ADS_TYPE_INT -> loadInterstitial(onAdsLoaded, onAdsLoadFailed)
            ADSType.ADS_TYPE_NAT -> loadNativeAdvertising(onAdsLoaded, onAdsLoadFailed)
            else -> onAdsLoadFailed.invoke("ad type not right")
        }
    }

    override fun show(activity: Activity, nativeParent: ViewGroup?, onAdsDismissed: () -> Unit) {
        val callback: FullScreenContentCallback by lazy {
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    onAdsDismissed.invoke()
                }

                override fun onAdShowedFullScreenContent() {
                    AdsManager.addShowCount()
                }

                override fun onAdFailedToShowFullScreenContent(e: AdError) {
                    onAdsDismissed.invoke()
                }
            }
        }

        fun showAdsFullScreen() {
            when (val ads = ad) {
                is InterstitialAd -> {
                    ads.run {
                        fullScreenContentCallback = callback
                        "Debug Logcat: Advertising adsType = ${adsType.adsItemType} type = ${item.adsType} Platform = ${item.adsPlatform}  ID = ${item.adsId} Showing".logE()
                        show(activity)
                    }
                }

                is AppOpenAd -> {
                    ads.run {
                        fullScreenContentCallback = callback
                        "Debug Logcat: Advertising adsType = ${adsType.adsItemType} type = ${item.adsType} Platform = ${item.adsPlatform}  ID = ${item.adsId} Showing".logE()
                        show(activity)
                    }
                }

                else -> onAdsDismissed.invoke()
            }
        }
        showAdsFullScreen()
    }

    private fun loadOpenAdvertising(onAdsLoaded: () -> Unit, onAdsLoadFailed: (msg: String?) -> Unit) {
        AppOpenAd.load(context, item.adsId, adsRequest, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, object : AppOpenAd.AppOpenAdLoadCallback() {
            override fun onAdLoaded(appOpenAd: AppOpenAd) {
                "Debug Logcat: Advertising adsType = ${adsType.adsItemType} type = ${item.adsType} Platform = ${item.adsPlatform}  ID = ${item.adsId} Loading".logE()
                ad = appOpenAd
                adsLoadTime = System.currentTimeMillis()
                onAdsLoaded.invoke()
                appOpenAd.setOnPaidEventListener {
                    onAdsPaid(it, item, appOpenAd.responseInfo)
                }
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                onAdsLoadFailed.invoke(loadAdError.message)
            }
        })
    }

    private fun loadInterstitial(onAdsLoaded: () -> Unit, onAdsLoadFailed: (msg: String?) -> Unit) {
        InterstitialAd.load(context, item.adsId, adsRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                "Debug Logcat: Advertising adsType = ${adsType.adsItemType}  type = ${item.adsType} Platform = ${item.adsPlatform}  ID = ${item.adsId} Loading".logE()
                ad = interstitialAd
                adsLoadTime = System.currentTimeMillis()
                onAdsLoaded.invoke()
                interstitialAd.setOnPaidEventListener {
                    onAdsPaid(it, item, interstitialAd.responseInfo)
                }
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                onAdsLoadFailed.invoke(loadAdError.message)
            }
        })
    }

    private fun loadNativeAdvertising(onAdsLoaded: () -> Unit, onAdsLoadFailed: (msg: String?) -> Unit) {

    }

}