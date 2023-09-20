package com.tqs.filemanager.vm.utils

import android.content.Context
import android.widget.Toast
import com.tqs.document.statistics.BuildConfig

fun String?.toast(context: Context) {
    if (BuildConfig.DEBUG) {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }
}