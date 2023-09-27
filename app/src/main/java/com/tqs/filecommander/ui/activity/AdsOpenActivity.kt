package com.tqs.filecommander.ui.activity

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tqs.filecommander.R
import com.tqs.filecommander.ads.AdsHelper
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.base.BaseActivity
import com.tqs.filecommander.databinding.ActivityAdsOpenBinding
import com.tqs.filecommander.notification.NotificationHelper
import com.tqs.filecommander.vm.activity.AdsOpenVM

class AdsOpenActivity : BaseActivity<ActivityAdsOpenBinding,AdsOpenVM>() {
    override val layoutId: Int
        get() = R.layout.activity_ads_open
    override val TAG: String
        get() = "AdsOpenActivity"


    override fun initData() {
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager ).apply {
            when(taskId){
                NotificationHelper.requestServiceCode,
                    NotificationHelper.requestBatteryCode,
                    NotificationHelper.requestUnlockCode,
                    NotificationHelper.requestUninstallCode -> cancel(taskId)
            }
        }
        AdsManager.adsFullScreen.showFullScreenAds(this){
            startActivity(Intent(this,ScannerActivity::class.java))
            finish()
        }
    }
}