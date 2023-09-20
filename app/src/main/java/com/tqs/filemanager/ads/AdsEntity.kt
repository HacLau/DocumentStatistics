package com.tqs.filemanager.ads

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class AdsEntity(
    @SerializedName("ujn")
    var showMax: Int = 40,
    @SerializedName("koof")
    var clickMax: Int = 15,
    @SerializedName("fc_launch")
    var adsOpen: MutableList<AdsItem>? = null,
    @SerializedName("fc_scan_int")
    var adsInsertResultScan: MutableList<AdsItem>? = null,
    @SerializedName("fc_result_int")
    var adsInsertResultClean: MutableList<AdsItem>? = null,
    @SerializedName("fc_main_nat")
    var adsNativeMain: MutableList<AdsItem>? = null,
    @SerializedName("fc_scan_nat")
    var adsNativeResultScan: MutableList<AdsItem>? = null,
    @SerializedName("fc_result_nat")
    var adsNativeResultClean: MutableList<AdsItem>? = null
)

data class AdsItem(
    @SerializedName("popl")
    var adsId: String,
    @SerializedName("gbne")
    var adsPlatform: String,
    @SerializedName("vtha")
    var adsType: String,
    @SerializedName("cgabk")
    var adsAliveMillis: Int,
    @SerializedName("buy")
    var adsWeight: Int
)

@Parcelize
data class AdsCount(
    var time: Long = System.currentTimeMillis(),
    var count: Int = 1
):Parcelable