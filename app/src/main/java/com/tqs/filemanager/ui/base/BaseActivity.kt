package com.tqs.filemanager.ui.base

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.tqs.document.statistics.R
import com.tqs.filemanager.ui.activity.DocListActivity
import com.tqs.filemanager.ui.activity.ImageListActivity
import com.tqs.filemanager.ui.activity.Not404Activity
import com.tqs.filemanager.ui.activity.ScannerResultActivity
import com.tqs.filemanager.vm.utils.Common
import com.tqs.filemanager.vm.utils.RepositoryUtils
import com.tqs.filemanager.vm.utils.logE


abstract class BaseActivity<VB: ViewDataBinding, VM: ViewModel> : AppCompatActivity() {
    protected lateinit var binding: VB
    protected lateinit var viewModel: VM
    abstract val layoutId: Int
    abstract val TAG: String
    private val currentNotAllowPermissions : MutableList<String> = Common.permissions.toMutableList()
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result: Map<String, Boolean> ->
        currentNotAllowPermissions.clear()
        for ((key,value) in result){
            if (!value){
                currentNotAllowPermissions.add(key)
            }
        }
        if (currentNotAllowPermissions.isEmpty()) {
            RepositoryUtils.requestPermission = true
            judgePermission()
        }
    }
    private val startActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        setResult(it.resultCode )
    }


    private val startActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        @RequiresApi(Build.VERSION_CODES.R)
        if (Environment.isExternalStorageManager()) {
            RepositoryUtils.requestCodeManager = true
            judgePermission()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        initData()
    }

    abstract fun initData()

    open fun setStatusBarLightMode(activity: Activity,isLightMode: Boolean) {
        val window = activity.window
        var option = window.decorView.systemUiVisibility
        option = if (isLightMode) {
            option or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            option and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        window.decorView.systemUiVisibility = option
    }
    open fun setStatusBarTransparent(activity: Activity) {
        val window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val option =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.decorView.systemUiVisibility = option
        window.statusBarColor = Color.TRANSPARENT
    }

    fun judgePermission(): Boolean {
        if (!RepositoryUtils.requestPermission) {
            requestPermission.launch(currentNotAllowPermissions.toTypedArray())
            return false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && (!Environment.isExternalStorageManager() || !RepositoryUtils.requestCodeManager)) {
            startActivity.launch(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                data = Uri.parse("package:${packageName}")
            })

            return false
        }
        onPermissionSuccess()
        return true
    }

    open fun onPermissionSuccess() {}

    fun jumpScannerResultActivity(fromPage : String){
        startActivityForResult.launch(Intent(this, ScannerResultActivity::class.java).apply {
            putExtra(Common.PAGE_TYPE, fromPage)
        })
        finish()
    }
    
    fun jumpMediaListActivity(fromPage:String){
        val intent = when(fromPage){
            Common.IMAGE_LIST,Common.VIDEO_LIST-> Intent(this, ImageListActivity::class.java)
            Common.DOCUMENTS_LIST,Common.DOWNLOAD_LIST,Common.AUDIO_LIST-> Intent(this, DocListActivity::class.java)
            else-> Intent(this, Not404Activity::class.java)
        }
        startActivityForResult.launch(intent.apply {
            putExtra(Common.PAGE_TYPE, fromPage)
        })
        finish()
    }
}