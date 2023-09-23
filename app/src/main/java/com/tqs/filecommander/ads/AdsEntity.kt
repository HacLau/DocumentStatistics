package com.tqs.filecommander.ads

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

enum class AdsItemType(val adsType: String) {
    ADSFULLSCREEN("fc_launch"),
    ADSINSERTRESULTSCAN("fc_scan_int"),
    ADSINSERTRESULTCLEAN("fc_result_int"),
    ADSNATIVEMAIN("fc_main_nat"),
    ADSNATIVERESULTSCAN("fc_scan_nat"),
    ADSNATIVERESULTCLEAN("fc_result_nat"),
}
object ADSType{
    const val ADS_TYPE_OP = "op"
    const val ADS_TYPE_INT = "int"
    const val ADS_TYPE_NAT = "nat"
}

object ADSPlatform{
    const val ADS_PLAT_ADMOB = "admob"
    const val ADS_PLAT_MAX = "max"
    const val ADS_PLAT_TOPON = "topon"
    const val ADS_PLAT_TRADPLUS = "tradplus"
}

object ADLTV{
    const val AdLTV_Top10Percent = "AdLTV_Top10Percent"
    const val AdLTV_Top20Percent = "AdLTV_Top20Percent"
    const val AdLTV_Top30Percent = "AdLTV_Top30Percent"
    const val AdLTV_Top40Percent = "AdLTV_Top40Percent"
    const val AdLTV_Top50Percent = "AdLTV_Top50Percent"
}