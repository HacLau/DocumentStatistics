package com.tqs.filemanager.ui.activity

import android.content.Intent
import android.os.CountDownTimer
import androidx.lifecycle.ViewModelProvider
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivitySplashBinding
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.vm.activity.SplashVM
import com.tqs.filemanager.ads.AdsManager
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

private const val COUNTER_TIME_MILLISECONDS = 5000L

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashVM>() {
    override val layoutId: Int
        get() = R.layout.activity_splash
    override val TAG: String
        get() = this.packageName
    private val isMobileAdsInitializeCalled = AtomicBoolean(false)
    private var secondsRemaining: Long = 0L
    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this).get(SplashVM::class.java)
        initAdsData()
        createTimer(COUNTER_TIME_MILLISECONDS)
    }

    private fun initAdsData() {
        AdsManager.adsOpen.preLoad(this)
        AdsManager.adsNativeMain.preLoad(this)
        AdsManager.adsInsertResultClean.preLoad(this)
        AdsManager.adsInsertResultScan.preLoad(this)
        AdsManager.adsNativeResultScan.preLoad(this)
        AdsManager.adsNativeResultClean.preLoad(this)
    }

    private fun createTimer(time: Long) {
        val countDownTimer: CountDownTimer =
            object : CountDownTimer(time, 33) {
                override fun onTick(millisUntilFinished: Long) {
                    secondsRemaining = TimeUnit.MILLISECONDS.toMillis(millisUntilFinished) + 1
                    binding.splashProgressBar.progress = 100 - (secondsRemaining / 50).toInt()
                }

                override fun onFinish() {
                    secondsRemaining = 0
                    binding.splashProgressBar.progress = 100
                    startMainActivity()
                }
            }
        countDownTimer.start()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }



}