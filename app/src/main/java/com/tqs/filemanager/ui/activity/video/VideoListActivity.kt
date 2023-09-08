package com.tqs.filemanager.ui.activity.video

import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivityVideoListBinding
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.vm.activity.video.VideoListVM

class VideoListActivity : BaseActivity<ActivityVideoListBinding, VideoListVM>() {
    override val layoutId: Int
        get() = R.layout.activity_video_list
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