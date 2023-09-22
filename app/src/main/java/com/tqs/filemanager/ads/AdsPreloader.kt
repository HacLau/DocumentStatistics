package com.tqs.filemanager.ads

import android.content.Context
import com.tqs.filemanager.vm.utils.logE

class AdsPreloader(
    private val context: Context,
    private val adsType: AdsItemType,
    private val source: MutableList<AdsItem>,
    private val cache: MutableList<BaseAds>,
    private val onLoad: (Boolean) -> Unit = {}
) {
    fun startPreload() {
        if (source.isEmpty()) {
            onLoad.invoke(false)
        } else {
            preloadAdsByIndex(0)
        }
    }

    private fun preloadAdsByIndex(index: Int) {
        val item = source.getOrNull(index)
        if (null == item) {
            onLoad.invoke(false)
            return
        }
        val baseAds = when (item.adsType) {
            ADSType.ADS_TYPE_INT, ADSType.ADS_TYPE_OP -> FullScreenAdvertising(context, adsType, item)
            ADSType.ADS_TYPE_NAT -> NativeMainAdvertising(context, adsType, item)
            else -> null
        }
        if (null == baseAds){
            onLoad.invoke(false)
            return
        }
        baseAds.load (onAdsLoaded = {
            cache.add(baseAds)
            cache.sortByDescending { it.adsItem.adsWeight }
            onLoad.invoke(true)
        }, onAdsLoadFailed = {
            preloadAdsByIndex(index + 1)
        })
    }

}