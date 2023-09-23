package com.tqs.filecommander.ui.activity

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.os.CountDownTimer
import androidx.lifecycle.ViewModelProvider
import com.tqs.filecommander.R
import com.tqs.filecommander.databinding.ActivityScannerBinding
import com.tqs.filecommander.ui.base.BaseActivity
import com.tqs.filecommander.vm.activity.ScannerVM
import com.tqs.filecommander.vm.utils.Common

class ScannerActivity : BaseActivity<ActivityScannerBinding, ScannerVM>() {
    override val layoutId: Int
        get() = R.layout.activity_scanner
    override val TAG: String
        get() = "ScannerActivity"
    private var mPageType: String = ""
    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        mPageType = intent.getStringExtra(Common.PAGE_TYPE).toString()
        viewModel = ViewModelProvider(this)[ScannerVM::class.java]
        binding.scannerAnim.addAnimatorListener(object : AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                createTimer(3000L)
            }

            override fun onAnimationEnd(animation: Animator) {
            }

            override fun onAnimationCancel(animation: Animator) {
                jumpScannerResultActivity(mPageType)

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