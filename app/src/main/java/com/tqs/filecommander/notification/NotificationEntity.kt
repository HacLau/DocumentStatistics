package com.tqs.filecommander.notification

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class NotificationEntity(
    @SerializedName("fcpop_switch")
    var notificationSwitch: Int = 0,
    @SerializedName("fcreffer")
    var referrerSwitch: Int = 0,
    @SerializedName("fc_t")
    var timing: NotificationItem,
    @SerializedName("fc_unl")
    var unclock: NotificationItem,
    @SerializedName("fc_char")
    var battery: NotificationItem,
    @SerializedName("fc_uni")
    var uninstall: NotificationItem,
)

data class NotificationItem(
    @SerializedName("open")
    var isPopup: Int = 1,
    @SerializedName("f")
    var delayPopupTime: Int = 5,
    @SerializedName("limit")
    var dayShowLimit: Int = 1,
    @SerializedName("interval")
    var intervalPopupTime: Int = 10
)

@Parcelize
data class NotificationShowed(
    var lastShowTime: Long = System.currentTimeMillis(),
    var showTimes: Int = 0
):Parcelable


object NotificationKey {
    const val TIMING = "fc_t"
    const val UNCLOCK = "fc_unl"
    const val BATTERY = "fc_char"
    const val UNINSTALL = "fc_uni"
}
