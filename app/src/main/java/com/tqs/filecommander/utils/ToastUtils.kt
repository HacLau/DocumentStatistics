package com.tqs.filecommander.utils

import android.content.Context
import android.widget.Toast
import com.tqs.filecommander.BuildConfig

fun String?.toast(context: Context) {
    if (BuildConfig.DEBUG) {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }
}