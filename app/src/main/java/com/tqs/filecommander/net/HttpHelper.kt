package com.tqs.filecommander.net

import com.tqs.filecommander.BuildConfig
import com.tqs.filecommander.utils.logE
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


const val DEFAULT_CONNECT_TIME = 10L
const val DEFAULT_WRITE_TIME = 30L
const val DEFAULT_READ_TIME = 30L

object HttpHelper {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(DEFAULT_CONNECT_TIME, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_WRITE_TIME, TimeUnit.SECONDS)
        .readTimeout(DEFAULT_READ_TIME, TimeUnit.SECONDS)
        .build()
    private var builder: Request.Builder? = null
    private val postType = MediaType.parse("application/json")

    fun sendRequestPost(
        baseUrl: String = BuildConfig.TBAUrl,
        jsonObject: JSONObject,
        resultSuccess: (String) -> Unit,
        resultFailed: (Int, String) -> Unit
    ) {
        "requestFromJson = $jsonObject".logE()

        thread {
            runCatching {
                val requestString = jsonObject.toString()
                "requestString = $requestString".logE()
                builder = Request.Builder().url(baseUrl)
                okHttpClient.newCall(
                    builder?.post(RequestBody.create(postType, requestString))?.build()
                ).execute().let {
                    it.toString().logE()
                    "code = ${it.code()}".logE()
                    if (it.isSuccessful) {
                        it.body().string().let { response ->
                            resultSuccess.invoke(response)
                        }
                    } else {
                        resultFailed.invoke(it.code(), it.message())
                    }
                }

            }
        }
    }

    fun sendRequestGet(
        baseUrl: String = BuildConfig.CloakUrl,
        jsonObject: JSONObject,
        resultSuccess: (String) -> Unit,
        resultFailed: (Int, String) -> Unit
    ) {
        thread {
            runCatching {
                val requestString = jsonObject.toString()
                "requestString = $requestString".logE()
                builder = Request.Builder().url(baseUrl)
                okHttpClient.newCall(builder?.get()?.build()).execute().let {
                    it.toString().logE()
                    "code = ${it.code()}".logE()
                    if (it.isSuccessful) {
                        it.body().string().let { response ->
                            resultSuccess.invoke(response)
                        }
                    } else {
                        resultFailed.invoke(it.code(), it.message())
                    }
                }
            }
        }
    }

}















