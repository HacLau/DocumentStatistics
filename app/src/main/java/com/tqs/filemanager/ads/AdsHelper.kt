package com.tqs.filemanager.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import com.tqs.filemanager.vm.utils.logE
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class AdsHelper(private val adsType: AdsItemType) {
    private val TAG = "AdsHelper"
    private val source: MutableList<AdsItem> = mutableListOf()
    private val cache: MutableList<BaseAds> = arrayListOf()
    private var isAdsLoading:Boolean = false
    private var onAdsLoad:(Boolean) ->  Unit = {}
    val isCacheNotEmpty: Boolean
        get() = cache.isNotEmpty()
    private fun getAdsCache(): BaseAds? = cache.removeFirstOrNull()

    fun initializeSource(data: MutableList<AdsItem>?) {
        source.run {
            clear()
            addAll(data ?: mutableListOf())
            sortByDescending { it.adsWeight }
        }
    }

    private fun isCacheOverTime(): Boolean {
        val item = cache.firstOrNull() ?: return false
        return if (System.currentTimeMillis() - item.adsLoadTime >= item.adsItem.adsAliveMillis * 6000L) {
            cache.remove(item)
            true
        } else {
            false
        }
    }

    fun preLoad(context: Context) {
        CoroutineScope(Dispatchers.Main + SupervisorJob() + CoroutineExceptionHandler { _, throwable -> Log.e(TAG, "${throwable.message}") }).launch {
            if (source.isEmpty()) return@launch
            if (AdsTimesManager.isOverLimit()) return@launch
            if (isCacheNotEmpty && isCacheOverTime().not()) return@launch
            if (isAdsLoading) return@launch
            "AdsHelper preload".logE()
            isAdsLoading = true
            AdsPreloader(context, adsType,source, cache){
                isAdsLoading = false
                onAdsLoad.invoke(it)
            }.startPreload()
        }
    }

    fun withLoad(context: Context,block:(Boolean)->Unit={}){
        if(isCacheNotEmpty && isCacheOverTime().not())
        block.invoke(true)
        else{
            onAdsLoad = block
            preLoad(context)
        }
    }


    fun showFullScreenAds(activity: Activity, onAdsDismissed: () -> Unit) {
        if (cache.isEmpty()){
            onAdsDismissed.invoke()
            return
        }
        val baseAds = getAdsCache()
        if (null == baseAds){
            onAdsDismissed.invoke()
            return
        }
        baseAds.show(activity = activity,onAdsDismissed = onAdsDismissed)
        onAdsLoad = {}
        preLoad(activity)
    }

    fun showNativeAds(activity: Activity, parent: ViewGroup?, onBaseAds: (BaseAds) -> Unit) {
        if (cache.isEmpty()){
            return
        }
        val baseAds = getAdsCache() ?: return
        baseAds.show(activity = activity,nativeParent = parent)
        onAdsLoad = {}
        preLoad(activity)
    }
}