package com.tqs.filemanager.ads

import android.util.Log
import com.blankj.utilcode.util.TimeUtils
import com.google.gson.Gson
import com.tqs.filemanager.vm.utils.RepositoryUtils


object AdsManager {
    const val TAG = "AdsManager"
    var showAdsCount = 0
    var clickAdsCount = 0
    private var adsEntity: AdsEntity? = null
    var adsFullScreen = AdsHelper(AdsItemType.ADSFULLSCREEN)
    var adsInsertResultScan = AdsHelper(AdsItemType.ADSINSERTRESULTSCAN)
    var adsInsertResultClean = AdsHelper(AdsItemType.ADSINSERTRESULTCLEAN)
    var adsNativeMain = AdsHelper(AdsItemType.ADSNATIVEMAIN)
    var adsNativeResultScan = AdsHelper(AdsItemType.ADSNATIVERESULTSCAN)
    var adsNativeResultClean = AdsHelper(AdsItemType.ADSNATIVERESULTCLEAN)

    fun initAdsConfig(adsJson: String) {
        Log.e(TAG, adsJson)
        adsEntity = Gson().fromJson(adsJson, AdsEntity::class.java)
        showAdsCount = adsEntity?.showMax ?: 0
        clickAdsCount = adsEntity?.clickMax ?: 0
        adsFullScreen.initializeSource(adsEntity?.adsOpen)
        adsInsertResultScan.initializeSource(adsEntity?.adsInsertResultScan)
        adsInsertResultClean.initializeSource(adsEntity?.adsInsertResultClean)
        adsNativeMain.initializeSource(adsEntity?.adsNativeMain)
        adsNativeResultScan.initializeSource(adsEntity?.adsNativeResultScan)
        adsNativeResultClean.initializeSource(adsEntity?.adsNativeResultClean)
    }

    fun isOverLimit(): Boolean {
        return clickOverLimit() || showOverLimit()
    }

    private fun showOverLimit(): Boolean {
        if (0 == AdsManager.showAdsCount) return false
        val showData = RepositoryUtils.showAdsData ?: return false

        return if (TimeUtils.isToday(showData.time)) showData.count >= AdsManager.showAdsCount else false
    }

    private fun clickOverLimit(): Boolean {
        if (0 == AdsManager.clickAdsCount) return false
        val clickData = RepositoryUtils.clickAdsData ?: return false

        return if (TimeUtils.isToday(clickData.time)) clickData.count >= AdsManager.clickAdsCount else false
    }

    fun addShowCount() {
        kotlin.runCatching {
            val showData = RepositoryUtils.showAdsData
            if (null == showData){
                RepositoryUtils.showAdsData = AdsCount()
            }else{
                if (TimeUtils.isToday(showData.time)){
                    showData.count ++
                    RepositoryUtils.showAdsData = showData
                }else
                    RepositoryUtils.showAdsData = AdsCount()
            }
        }
    }

    fun addClickCount() {
        kotlin.runCatching {
            val clickData = RepositoryUtils.clickAdsData
            if (null == clickData){
                RepositoryUtils.clickAdsData = AdsCount()
            }else{
                if (TimeUtils.isToday(clickData.time)){
                    clickData.count ++
                    RepositoryUtils.clickAdsData = clickData
                }else
                    RepositoryUtils.clickAdsData = AdsCount()
            }
        }
    }
}