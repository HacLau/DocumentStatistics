package com.tqs.filemanager.ads

import android.app.Activity
import android.content.Context
import android.view.ViewGroup

class AdsNativeResultScan(
    private val context: Context,
    private val adsType: AdsType,
    private val item: AdsItem
) : BaseAds(adsType, item) {
    override fun load(onAdLoaded: () -> Unit, onAdLoadFailed: (msg: String?) -> Unit) {

    }

    override fun show(activity: Activity, nativeParent: ViewGroup?, onAdDismissed: () -> Unit) {

    }

}