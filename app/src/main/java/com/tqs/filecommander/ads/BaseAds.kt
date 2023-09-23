package com.tqs.filecommander.ads

import android.app.Activity
import android.view.ViewGroup
import com.google.android.gms.ads.AdValue
import com.google.android.gms.ads.ResponseInfo
import com.google.firebase.analytics.FirebaseAnalytics
import com.tqs.filecommander.tba.TBAHelper

abstract class BaseAds(
    private val adsType: AdsItemType,
    val adsItem: AdsItem,
    var adsLoadTime: Long = System.currentTimeMillis()
) {
    abstract fun load(onAdsLoaded: () -> Unit = {}, onAdsLoadFailed: (msg: String?) -> Unit = {})
    abstract fun show(activity: Activity, nativeParent: ViewGroup? = null, onAdsDismissed: () -> Unit = {})

    //paid data to firebase
    fun onAdsPaid(adsValue: AdValue, item: AdsItem, responseInfo: ResponseInfo?) {
        // data update to firebase
        runCatching {
            logEvent(
                "ad_impression_revenue",
                mutableMapOf(
                    FirebaseAnalytics.Param.VALUE to adsValue.valueMicros / 1000000.toDouble(),
                    FirebaseAnalytics.Param.CURRENCY to "USD",
                    "precisionType" to adsValue.precisionType,
                    "adNetwork" to responseInfo?.mediationAdapterClassName
                )
            )
        }

        // todo data update to operate platform ad_impression
        TBAHelper.updateAdvertising(adsType = item.adsType, adsId = item.adsId, adsIndex = adsType.adsItemType, adsSDK = item.adsPlatform)
    }

    open fun destroyNative() {}
}