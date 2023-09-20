package com.tqs.filemanager.vm.utils

import android.util.Log
import com.tqs.document.statistics.BuildConfig

fun String.logE(){
    if (BuildConfig.DEBUG){
        Log.e("File Commander",this)
    }
}
fun String.logW(){
    if (BuildConfig.DEBUG){
        Log.w("File Commander",this)
    }
}
fun String.logD(){
    if (BuildConfig.DEBUG){
        Log.d("File Commander",this)
    }
}

fun String.logI(){
    if (BuildConfig.DEBUG){
        Log.i("File Commander",this)
    }
}