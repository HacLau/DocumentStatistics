package com.tqs.filemanager.vm.utils

import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat


object DateUtils {
    fun millis2yyMM(millis: Long): String {
        val currentTime: String = SimpleDateFormat("yyyy-MM").format(millis)
        return "$currentTime"
    }

    fun second2yyMM(millis: Long): String {
        return millis2yyMM(millis * 1000)
    }
}