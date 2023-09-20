package com.tqs.filemanager.ui.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import com.tqs.filemanager.vm.utils.RepositoryUtils
import com.tqs.filemanager.vm.utils.SharedUtils
import com.tqs.filemanager.vm.utils.logE

class MainActivity : BaseActivity<ActivityMainBinding, MainVM>(), View.OnClickListener {

    override val layoutId: Int get() = R.layout.activity_main
    override val TAG: String
        get() = this.packageName
    private var navController: NavController? = null

    override fun initData() {
        setContentView(binding.root)
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)?.let {
            navController = Navigation.findNavController(this, it.id)
        }
        navController?.setGraph(R.navigation.mobile_navigation)
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
                selectedMenu(R.id.nav_file_manager, false)
            }

            R.id.rl_contact_us -> {
                jumpToContactUs()
            }

            R.id.rl_privacy_policy -> {
                selectedMenu(R.id.nav_privacy_policy, false)
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

    private fun selectedMenu(resID: Int, boolean: Boolean) {
        if (boolean) {
            selectedMenu(resID)
        } else {
            if (navController?.currentDestination?.id != resID) {
                selectedMenu(resID)
            }
        }
    }

    private fun selectedMenu(resID: Int) {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        navController?.popBackStack(resID, true)
        navController?.navigate(resID)

    }

    override fun onPermissionSuccess() {
        "onPermissionSuccess".logE()
        selectedMenu(R.id.nav_file_manager, true)
    }

}