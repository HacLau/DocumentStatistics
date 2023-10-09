package com.tqs.filecommander.ui.activity

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.tqs.filecommander.R
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.databinding.ActivityScannerBinding
import com.tqs.filecommander.base.BaseActivity
import com.tqs.filecommander.notification.NotificationKey
import com.tqs.filecommander.tba.EventPoints
import com.tqs.filecommander.tba.TBAHelper
import com.tqs.filecommander.utils.Common
import com.tqs.filecommander.vm.MainVM

class ScannerActivity : BaseActivity<ActivityScannerBinding, MainVM>() {
    override val layoutId: Int
        get() = R.layout.activity_scanner
    override val TAG: String
        get() = "ScannerActivity"

    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this)[MainVM::class.java]
        viewModel.mPageType = intent.getStringExtra(Common.PAGE_TYPE).toString()
        val notifyType = intent.getStringExtra(NotificationKey.notifyType)
        TBAHelper.updatePoints(
            EventPoints.filecpop_all_page, mutableMapOf(
                EventPoints.source to when (notifyType) {
                    NotificationKey.SCHEDULED -> "t"
                    NotificationKey.CHARGE -> "char"
                    NotificationKey.UNCLOCK -> "unl"
                    NotificationKey.UNINSTALL -> "uni"
                    else -> {
                        ""
                    }
                }
            )
        )

        TBAHelper.updatePoints(EventPoints.filec_clean_show)
        binding.scannerAnim.addAnimatorListener(object : AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                // loop a day
                createTimer(viewModel.countDownTime)
            }

            override fun onAnimationEnd(animation: Animator) {
            }

            override fun onAnimationCancel(animation: Animator) {
                AdsManager.adsInsertResultScan.showFullScreenAds(this@ScannerActivity) {
                    Log.e(TAG, "ads onDismiss")
                    jumpScannerResultActivity(viewModel.mPageType, notifyType)
                }
            }

            override fun onAnimationRepeat(animation: Animator) {
            }

        })
    }

    private var count = 0
    private fun createTimer(time: Long) {
        val countDownTimer: CountDownTimer =
            object : CountDownTimer(time, 33) {
                override fun onTick(millisUntilFinished: Long) {
                    binding.scannerTips.text = "Scanner${
                        when (count % 40) {
                            in 0..<10 -> "."
                            in 10..<20 -> ".."
                            in 20..29 -> "..."
                            else -> "...."
                        }
                    }"
                    count++
                    if (millisUntilFinished < 3000L && AdsManager.adsNativeMain.isCacheNotEmpty) {
                        this.cancel()
                        this.onFinish()
                    }
                }

                override fun onFinish() {
                    binding.scannerAnim.cancelAnimation()
                }
            }
        countDownTimer.start()
    }

    override fun onBackPressed() {

    }
}