package com.tqs.filemanager.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.tqs.document.statistics.BuildConfig
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivityMainBinding
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.ui.view.TitleBar
import com.tqs.filemanager.vm.activity.MainVM
import com.tqs.filemanager.vm.utils.Common
import com.tqs.filemanager.vm.utils.SharedUtils

class MainActivity : BaseActivity<ActivityMainBinding, MainVM>(), View.OnClickListener {

    override val layoutId: Int get() = R.layout.activity_main
    override val TAG: String
        get() = this.packageName
    private lateinit var navController: NavController
    private var REQUEST_CODE_MANAGE_EXTERNAL_STORAGE = 0x00098
    val requestPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result: Map<String, Boolean> ->

    }

    override fun initData() {
        setContentView(binding.root)
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)?.let {
            navController = Navigation.findNavController(this, it.id)
        }
        navController?.let {
            navController.setGraph(R.navigation.mobile_navigation)
        }
        binding.appBarMain.findViewById<TitleBar>(R.id.title_bar_main).setLeftClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.clFileManager.setOnClickListener(this)
        binding.rlContactUs.setOnClickListener(this)
        binding.rlPrivacyPolicy.setOnClickListener(this)
        binding.rlShare.setOnClickListener(this)
        binding.rlUpgrade.setOnClickListener(this)

        judgePermission()

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cl_file_manager -> {
                selectedMenu(R.id.nav_file_manager)
            }

            R.id.rl_contact_us -> {
                jumpToContactUs()
            }

            R.id.rl_privacy_policy -> {
                selectedMenu(R.id.nav_privacy_policy)
            }

            R.id.rl_share -> {
                jumpToShareApp()
            }

            R.id.rl_upgrade -> {
                jumpToUpdate()
            }
        }
    }

    private fun jumpToUpdate() {
        kotlin.runCatching {
            startActivity(Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
                setPackage("com.android.vending")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    private fun jumpToShareApp() {
        kotlin.runCatching {
            startActivity(Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
            })
        }
    }

    private fun jumpToContactUs() {
        kotlin.runCatching {
            startActivity(Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:${Common.EMAIL}")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    private fun selectedMenu(resID: Int) {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        // judge current id is or not need replace id,if equals do not
        if (navController.currentDestination?.id != resID) {
            // param inclusive = true means is pop stack contains resID self
            navController.popBackStack(resID, true)
            navController.navigate(resID)
        }
    }

    override fun permissionSuccess(requestCode: Int) {
        super.permissionSuccess(requestCode)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            SharedUtils.putValue(this, Common.EXTERNAL_STORAGE_PERMISSION, true)
            judgePermission()
        }
    }

    override fun permissionFail(requestCode: Int) {
        super.permissionFail(requestCode)
    }

    private fun judgePermission(): Boolean {
        if (!(SharedUtils.getValue(this, Common.EXTERNAL_STORAGE_PERMISSION, false) as Boolean)) {
            requestPermission.launch(Common.permissions)
            return false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()
            && !(SharedUtils.getValue(this, Common.REQUEST_CODE_MANAGE_EXTERNAL_STORAGE, false) as Boolean)
        ) {
            val startActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    if (Environment.isExternalStorageManager()) {
                        SharedUtils.putValue(this, Common.REQUEST_CODE_MANAGE_EXTERNAL_STORAGE, true)
                        judgePermission()
                    }
                }
            }
            startActivity.launch(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                data = Uri.parse("package:${packageName}")
            })

            return false
        }
        selectedMenu(R.id.nav_file_manager)
        return true
    }


    override fun onBackPressed() {
        super.onBackPressed()
        Log.e(TAG, "onBackPressed ${navController.backQueue.firstOrNull()?.id}")
    }

}