package com.tqs.filemanager.ads

import android.app.Activity
import android.view.ViewGroup
import com.google.android.gms.ads.AdValue
import com.google.android.gms.ads.ResponseInfo

abstract class BaseAds(
    private val adsType: AdsType,
    val adsItem: AdsItem,
    var adsLoadTime: Long = System.currentTimeMillis()
) {
    abstract fun load(onAdLoaded: () -> Unit = {}, onAdLoadFailed: (msg: String?) -> Unit = {})
    abstract fun show(activity: Activity, nativeParent: ViewGroup? = null, onAdDismissed: () -> Unit = {})

    fun onAdsPaid(adsValue:AdValue, item:AdsItem, responseInfo:ResponseInfo?){

    }
}