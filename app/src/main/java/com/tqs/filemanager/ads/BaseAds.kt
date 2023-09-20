package com.tqs.filemanager.ads

import android.app.Activity
import android.view.ViewGroup
import com.google.android.gms.ads.AdValue
import com.google.android.gms.ads.ResponseInfo

abstract class BaseAds(
    private val adsType: AdsItemType,
    val adsItem: AdsItem,
    var adsLoadTime: Long = System.currentTimeMillis()
) {
    abstract fun load(onAdsLoaded: () -> Unit = {}, onAdsLoadFailed: (msg: String?) -> Unit = {})
    abstract fun show(activity: Activity, nativeParent: ViewGroup? = null, onAdsDismissed: () -> Unit = {})

    fun onAdsPaid(adsValue:AdValue, item:AdsItem, responseInfo:ResponseInfo?){

    }

    open fun destroyNative(){}
}