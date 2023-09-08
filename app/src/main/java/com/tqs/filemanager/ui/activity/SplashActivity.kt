package com.tqs.filemanager.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivitySplashBinding
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.vm.activity.SplashVM
import java.util.Timer
import java.util.TimerTask


class SplashActivity : BaseActivity<ActivitySplashBinding,SplashVM>() {
    override val layoutId: Int
        get() = R.layout.activity_splash
    override val TAG: String
        get() = this.packageName
    private var timer: Timer? = null
    private var task: TimerTask? = null
    private var REQUEST_CODE_PERMISSION = 0x00099
    var permissions = arrayOf<String>(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private val handler: Handler = object :Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1){
                Log.e(TAG,"what = ${msg.what} and ${viewModel.progressValue.value}")
                viewModel.progressValue.value = viewModel.progressValue.value?.plus(1)
            }
        }
    }
    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this).get(SplashVM::class.java)
        viewModel.progressValue.observe(this){
            binding.splashProgressBar.progress = it
            if (it > 100){
                toJumpMainActivity()
                timer?.cancel()
            }
        }
        timer = Timer()
        task = object : TimerTask() {
            override fun run() {
                try {
                    val message = Message()
                    message.what = 1
                    handler.sendMessage(message)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        requestPermission(permissions,REQUEST_CODE_PERMISSION)
    }

    private fun startTimer() {
        timer?.schedule(task,20, 20)
    }


    private fun toJumpMainActivity(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer = null
    }

    override fun permissionSuccess(requestCode: Int) {
        super.permissionSuccess(requestCode)
        if (requestCode == REQUEST_CODE_PERMISSION){
            Toast.makeText(this,"get permissions success",Toast.LENGTH_SHORT).show()
            startTimer()
        }
    }

    override fun permissionFail(requestCode: Int) {
        super.permissionFail(requestCode)
        Toast.makeText(this,"get permissions fail",Toast.LENGTH_SHORT).show()
        startTimer()
    }


}