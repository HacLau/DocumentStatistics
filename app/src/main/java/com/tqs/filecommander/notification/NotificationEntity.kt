package com.tqs.filecommander.notification

import com.google.gson.annotations.SerializedName

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
    var charge: NotificationItem,
    @SerializedName("fc_uni")
    var unload: NotificationItem,
)

data class NotificationItem(
    @SerializedName("open")
    var isPopup: Int = 1,
    @SerializedName("f")
    var delayPopupTime: Int = 5,
    @SerializedName("limit")
    var dayShowLimit: Int = 1,
    @SerializedName("interval")
    var intervalPopupTime: Int = 10,
)

object NotificationKey {
    const val TIMING = "fc_t"
    const val UNCLOCK = "fc_unl"
    const val CHARGE = "fc_char"
    const val UNLOAD = "fc_uni"
}
