package com.tqs.filemanager.ui.activity

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivityMainBinding
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.vm.activity.MainVM

class MainActivity : BaseActivity<ActivityMainBinding,MainVM>(), View.OnClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    override val layoutId: Int
        get() = R.layout.activity_main
    override val TAG: String
        get() = this.packageName
    private lateinit var navController: NavController
    override fun initData() {
        setContentView(binding.root)
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_file_manager,R.id.nav_contact_us, R.id.nav_privacy_policy, R.id.nav_share, R.id.nav_upgrade
            ), binding.drawerLayout
        )

        binding.appBarMain.findViewById<ImageView>(R.id.iv_order).setOnClickListener {
            binding.drawerLayout.openDrawer(Gravity.LEFT)
        }
        binding.clFileManager.setOnClickListener(this)
        binding.rlContactUs.setOnClickListener(this)
        binding.rlPrivacyPolicy.setOnClickListener(this)
        binding.rlShare.setOnClickListener(this)
        binding.rlUpgrade.setOnClickListener(this)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.cl_file_manager -> {
                selectedMenu(0)
                navController.navigate(R.id.nav_file_manager)
            }
            R.id.rl_contact_us -> {
                selectedMenu(1)
                navController.navigate(R.id.nav_contact_us)
            }
            R.id.rl_privacy_policy -> {
                selectedMenu(2)
                navController.navigate(R.id.nav_privacy_policy)
            }
            R.id.rl_share -> {
                selectedMenu(3)
                navController.navigate(R.id.nav_share)
            }
            R.id.rl_upgrade -> {
                selectedMenu(4)
                navController.navigate(R.id.nav_upgrade)
            }
        }
    }

    private fun selectedMenu(index: Int){
        binding.drawerLayout.closeDrawer(Gravity.LEFT)
    }

}