package com.tqs.filecommander.ui.activity

import android.app.NotificationManager
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.tqs.filecommander.R
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.base.BaseActivity
import com.tqs.filecommander.databinding.ActivityAdsOpenBinding
import com.tqs.filecommander.notification.NotificationHelper
import com.tqs.filecommander.utils.Common
import com.tqs.filecommander.vm.MainVM

class AdsOpenActivity : BaseActivity<ActivityAdsOpenBinding, MainVM>() {
    override val layoutId: Int
        get() = R.layout.activity_ads_open
    override val TAG: String
        get() = "AdsOpenActivity"


    override fun initData() {
        viewModel = ViewModelProvider(this)[MainVM::class.java]
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager ).apply {
            when(taskId){
                NotificationHelper.requestServiceCode,
                    NotificationHelper.requestBatteryCode,
                    NotificationHelper.requestUnlockCode,
                    NotificationHelper.requestUninstallCode -> cancel(taskId)
            }
        }
        AdsManager.adsFullScreen.showFullScreenAds(this){
            jumpScannerActivity(Common.pageArray[(0..4).random()])
        }
    }
}