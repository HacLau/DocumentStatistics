package com.tqs.filemanager.ads

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions

class NativeMainAdvertising(
    private val context: Context,
    private val adsType: AdsItemType,
    private val item: AdsItem
) : BaseAds(adsType, item) {
    private var nativeAd: NativeAd? = null
    private val adRequest: AdRequest get() = AdRequest.Builder().build()
    override fun load(onAdLoaded: () -> Unit, onAdLoadFailed: (msg: String?) -> Unit) {
        AdLoader.Builder(context, item.adsId).apply {
            forNativeAd {
                nativeAd = it
                adsLoadTime = System.currentTimeMillis()
                onAdLoaded.invoke()
                it.setOnPaidEventListener { value ->
                    onAdsPaid(value, item, it.responseInfo)
                }
            }
            withAdListener(object :AdListener(){
                override fun onAdClicked() = AdsTimesManager.addClickCount()
                override fun onAdFailedToLoad(loadAdError: LoadAdError) = onAdLoadFailed.invoke(loadAdError.message)
            })
            withNativeAdOptions(NativeAdOptions.Builder().apply {
                setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
            }.build())
        }.build().loadAd(adRequest)
    }

    override fun show(activity: Activity, nativeParent: ViewGroup?, onAdDismissed: () -> Unit) {

    }

    override fun destroyNative() {
        nativeAd?.destroy()
        nativeAd = null
    }

}