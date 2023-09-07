package com.tqs.filemanager.ui.activity

import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivityMainBinding
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.vm.activity.MainVM

class MainActivity : BaseActivity<ActivityMainBinding,MainVM>() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    override val layoutId: Int
        get() = R.layout.activity_main
    override val TAG: String
        get() = this.packageName

    override fun initData() {
        setContentView(binding.root)
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_contact_us, R.id.nav_privacy_policy, R.id.nav_share, R.id.nav_upgrade
            ), drawerLayout
        )

        binding.navView.setupWithNavController(navController)
        binding.appBarMain.findViewById<ImageView>(R.id.iv_order).setOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}