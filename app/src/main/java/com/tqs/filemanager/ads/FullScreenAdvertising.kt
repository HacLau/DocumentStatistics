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

    override fun show(activity: Activity, nativeParent: ViewGroup?, onAdDismissed: () -> Unit) {
        fun onAdsClose() {
            "AdsOpen show onAdsClose".logE()
            onAdDismissed.invoke()
        }

        fun onAdsShowedSuccess() {
            "AdsOpen show onAdsShowedSuccess".logE()
            AdsTimesManager.addShowCount()
        }

        fun onAdsFailedToShow(msg: String?) {
            "AdsOpen show onAdsFailedToShow".logE()
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
                    "AdsOpen show showAdsFullScreen InterstitialAd".logE()
                    ads.run {
                        fullScreenContentCallback = callback
                        show(activity)
                    }
                }

                is AppOpenAd -> {
                    "AdsOpen show showAdsFullScreen AppOpenAd".logE()
                    ads.run {
                        fullScreenContentCallback = callback
                        show(activity)
                    }
                }

                else -> onAdDismissed.invoke()
            }
        }
        showAdsFullScreen()
    }

    private fun loadOpenAdvertising(onAdsLoaded: () -> Unit, onAdsLoadFailed: (msg: String?) -> Unit) {
        "AdsOpen load open advertising".logE()
        AppOpenAd.load(context, item.adsId, adsRequest, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, object : AppOpenAd.AppOpenAdLoadCallback() {
            override fun onAdLoaded(appOpenAd: AppOpenAd) {
                "AdsOpen load open advertising  onAdLoaded".logE()
                ad = appOpenAd
                adsLoadTime = System.currentTimeMillis()
                onAdsLoaded.invoke()
                appOpenAd.setOnPaidEventListener {
                    "AdsOpen load open advertising  onAdLoaded setOnPaidEventListener".logE()
                    onAdsPaid(it, item, appOpenAd.responseInfo)
                }
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                "AdsOpen load open advertising  onAdFailedToLoad".logE()
                onAdsLoadFailed.invoke(loadAdError.message)
            }
        })
    }

    private fun loadInterstitial(onAdsLoaded: () -> Unit, onAdsLoadFailed: (msg: String?) -> Unit) {
        "AdsOpen load Interstitial advertising".logE()
        InterstitialAd.load(context, item.adsId, adsRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                "AdsOpen load Interstitial advertising onAdLoaded".logE()
                ad = interstitialAd
                adsLoadTime = System.currentTimeMillis()
                onAdsLoaded.invoke()
                interstitialAd.setOnPaidEventListener {
                    "AdsOpen load Interstitial advertising setOnPaidEventListener".logE()
                    onAdsPaid(it, item, interstitialAd.responseInfo)
                }
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                "AdsOpen load Interstitial advertising onAdFailedToLoad".logE()
                onAdsLoadFailed.invoke(loadAdError.message)
            }
        })
    }

    private fun loadNativeAdvertising(onAdsLoaded: () -> Unit, onAdsLoadFailed: (msg: String?) -> Unit) {

    }

}