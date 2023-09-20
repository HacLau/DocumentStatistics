package com.tqs.filemanager.ads

import android.content.Context
import com.blankj.utilcode.util.TimeUtils
import com.tqs.filemanager.vm.utils.RepositoryUtils

object AdsTimesManager {

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