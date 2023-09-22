package com.tqs.filemanager.vm.utils

import com.tqs.filemanager.ads.AdsCount

object RepositoryUtils:MMKVOwner(mapId = "fileCommander") {
    var requestPermission by mmkvBoolean(default = false)
    var requestCodeManager by mmkvBoolean(default = false)
    var showAdsData by mmkvParcelable<AdsCount>()
    var clickAdsData by mmkvParcelable<AdsCount>()
    var installReferrer by mmkvString(default = "")
}