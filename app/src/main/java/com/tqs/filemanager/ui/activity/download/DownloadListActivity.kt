package com.tqs.filemanager.ui.activity.download

import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivityDownloadListBinding
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.vm.activity.download.DownloadListVM

class DownloadListActivity : BaseActivity<ActivityDownloadListBinding, DownloadListVM>() {
    override val layoutId: Int
        get() = R.layout.activity_download_list
    override val TAG: String
        get() = this.packageName
    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        binding.titleBar.setLeftClickListener {
            finish()
        }
    }
}