package com.tqs.filecommander.ui.activity

import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.tqs.filecommander.R
import com.tqs.filecommander.databinding.ActivitySplashBinding
import com.tqs.filecommander.base.BaseActivity
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.tba.EventPoints
import com.tqs.filecommander.tba.TBAHelper
import com.tqs.filecommander.utils.TimerUtils
import com.tqs.filecommander.utils.logI
import com.tqs.filecommander.vm.MainVM

class SplashActivity : BaseActivity<ActivitySplashBinding, MainVM>() {
    override val layoutId: Int
        get() = R.layout.activity_splash
    override val TAG: String
        get() = this.packageName

    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this)[MainVM::class.java]
        initAdsData()
        TimerUtils.startCountDownTimer(viewModel.countDownTime, {
            binding.splashProgressBar.progress = 100 - (it / 80).toInt()
        }) {
            binding.splashProgressBar.progress = 100
            AdsManager.adsFullScreen.showFullScreenAds(this@SplashActivity) {
                Log.e(TAG, "ads onDismiss")

                startMainActivity()
            }
        }
        TBAHelper.updatePoints(EventPoints.filec_startpage_show)
        TBAHelper.updatePoints(EventPoints.filec_launch_page_show)
    }

    private fun initAdsData() {
        AdsManager.adsFullScreen.preLoad(this)
        AdsManager.adsNativeMain.preLoad(this)
        AdsManager.adsInsertResultClean.preLoad(this)
        AdsManager.adsInsertResultScan.preLoad(this)
        AdsManager.adsNativeResultScan.preLoad(this)
        AdsManager.adsNativeResultClean.preLoad(this)
    }

}