package com.tqs.filecommander.vm.utils

import com.tqs.filecommander.ads.AdsCount

object RepositoryUtils:MMKVOwner(mapId = "fileCommander") {
    var requestPermission by mmkvBoolean(default = false)
    var requestCodeManager by mmkvBoolean(default = false)
    var showAdsData by mmkvParcelable<AdsCount>()
    var clickAdsData by mmkvParcelable<AdsCount>()
    var installReferrer by mmkvString(default = "")
}