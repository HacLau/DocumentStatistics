package com.tqs.filemanager.ads

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
import com.tqs.filemanager.vm.utils.logE

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
        fun onAdsClose() {
            onAdsDismissed.invoke()
        }

        fun onAdsShowedSuccess() {
            AdsManager.addShowCount()
        }

        fun onAdsFailedToShow(msg: String?) {
            onAdsClose()
        }

        val callback: FullScreenContentCallback by lazy {
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() = onAdsClose()
                override fun onAdShowedFullScreenContent() = onAdsShowedSuccess()
                override fun onAdFailedToShowFullScreenContent(e: AdError) = onAdsFailedToShow(e.message)
            }
        }

        fun showAdsFullScreen() {
            when (val ads = ad) {
                is InterstitialAd -> {
                    ads.run {
                        fullScreenContentCallback = callback
                        show(activity)
                    }
                }

                is AppOpenAd -> {
                    ads.run {
                        fullScreenContentCallback = callback
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