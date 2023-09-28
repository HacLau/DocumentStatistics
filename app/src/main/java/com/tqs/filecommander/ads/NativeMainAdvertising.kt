package com.tqs.filecommander.ads

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.tqs.filecommander.R
import com.tqs.filecommander.base.BaseAds
import com.tqs.filecommander.databinding.LayoutAdvertisingNativeBinding

class NativeMainAdvertising(
    private val context: Context,
    private val adsType: AdsItemType,
    private val item: AdsItem
) : BaseAds(adsType, item) {
    private var nativeAd: NativeAd? = null
    private val adRequest: AdRequest get() = AdRequest.Builder().build()
    override fun load(onAdsLoaded: () -> Unit, onAdsLoadFailed: (msg: String?) -> Unit) {
        AdLoader.Builder(context, item.adsId).apply {
            forNativeAd {
                nativeAd = it
                adsLoadTime = System.currentTimeMillis()
                onAdsLoaded.invoke()
                it.setOnPaidEventListener { value ->
                    onAdsPaid(value, item, it.responseInfo)
                }
            }
            withAdListener(object : AdListener() {
                override fun onAdClicked() = AdsManager.addClickCount()
                override fun onAdFailedToLoad(loadAdError: LoadAdError) = onAdsLoadFailed.invoke(loadAdError.message)
            })
            withNativeAdOptions(NativeAdOptions.Builder().apply {
                setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_LEFT)
            }.build())
        }.build().loadAd(adRequest)
    }

    override fun show(activity: Activity, nativeParent: ViewGroup?, onAdsDismissed: () -> Unit) {
        val binding: LayoutAdvertisingNativeBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_advertising_native, nativeParent, false)
        binding.nativeAdView.mediaView = binding.nativeMediaView
        binding.nativeAdView.headlineView = binding.nativeAppTitle
        binding.nativeAdView.bodyView = binding.nativeAppDes
        binding.nativeAdView.iconView = binding.nativeAppIcon
        binding.nativeAdView.callToActionView = binding.nativeAppInstall

        nativeAd?.mediaContent?.let {
            binding.nativeMediaView.mediaContent = it
        }
        nativeAd?.icon?.drawable?.let {
            binding.nativeAppIcon.setImageDrawable(it)
        }

        binding.nativeAppTitle.text = nativeAd?.headline
        binding.nativeAppDes.text = nativeAd?.body
        binding.nativeAppInstall.text = nativeAd?.callToAction

        nativeAd?.let { binding.nativeAdView.setNativeAd(it) }

        nativeParent?.isVisible = true
        nativeParent?.removeAllViews()
        nativeParent?.addView(binding.nativeMediaView)
        AdsManager.addShowCount()
    }

    override fun destroyNative() {
        nativeAd?.destroy()
        nativeAd = null
    }

}