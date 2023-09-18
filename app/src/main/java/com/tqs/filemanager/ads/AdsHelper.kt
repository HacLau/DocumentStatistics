package com.tqs.filemanager.ads

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class AdsHelper(private val type: AdsType) {
    private val TAG = "AdsHelper"
    private val source:MutableList<AdsItem> = mutableListOf()
    fun initializeSource(data: MutableList<AdsItem>?) {
        source.run {
            clear()
            addAll(data ?: mutableListOf())
            sortByDescending { it.adsWeight }
        }
    }

    fun preLoad(context: Context){
        CoroutineScope(Dispatchers.Main + SupervisorJob() + CoroutineExceptionHandler { _, throwable -> Log.e(TAG,"${throwable.message}") }).launch {
            if (source.isEmpty()) return@launch

        }
    }
}