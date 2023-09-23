package com.tqs.filecommander.vm.utils

import android.content.Context
import android.content.SharedPreferences

object SharedUtils {
    private val FILE_NAME = "file_manager"
    fun <T> putValue(
        context: Context,
        key: String,
        value: T,
        fileName: String = FILE_NAME
    ) {
        getEditor(fileName, context)?.let { editor ->
            when (value) {
                is String -> {
                    editor.putString(key, value)
                }

                is Boolean -> {
                    editor.putBoolean(key, value)
                }

                is Int -> {
                    editor.putInt(key, value)
                }

                is Long -> {
                    editor.putLong(key, value)
                }

                is Float -> {
                    editor.putFloat(key, value)
                }

                is Enum<*> -> {
                    editor.putString(key, value.name)
                }
            }
            editor.apply()
        }
    }

    fun <T> getValue(
        context: Context,
        key: String,
        defaultValue: T,
        fileName: String = FILE_NAME
    ): Comparable<*>? {
        val sharedPreferences =
            context.applicationContext.getSharedPreferences("sp_$fileName", Context.MODE_PRIVATE)
        sharedPreferences?.let {
            return when (defaultValue) {
                is String -> {
                    it.getString(key, defaultValue)
                }

                is Boolean -> {
                    it.getBoolean(key, defaultValue)
                }

                is Int -> {
                    it.getInt(key, defaultValue)
                }

                is Long -> {
                    it.getLong(key, defaultValue)
                }

                is Float -> {
                    it.getFloat(key, defaultValue)
                }

                is Enum<*> -> {
                    it.getString(key, defaultValue.name)
                }

                else -> {
                    throw IllegalArgumentException("This type of data connot be saved!")
                }
            }
        }
        return null
    }

    fun remove(
        context: Context,
        key: String,
        fileName: String = FILE_NAME
    ) {
        getEditor(fileName, context)?.remove(key)
    }

    fun clear(
        context: Context,
        key: String,
        fileName: String = FILE_NAME
    ) {
        getEditor(fileName, context)?.clear()
    }

    private fun getEditor(fileName: String, context: Context): SharedPreferences.Editor? {
        val sharedPreferences = context.applicationContext.getSharedPreferences(
            "sp_$fileName",
            Context.MODE_PRIVATE
        )
        return sharedPreferences?.edit()
    }
}