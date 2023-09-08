package com.tqs.filemanager.ui.activity.video

import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivityVideoPreviewBinding
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.vm.activity.video.VideoPreviewVM

class VideoPreviewActivity : BaseActivity<ActivityVideoPreviewBinding, VideoPreviewVM>() {
    override val layoutId: Int
        get() = R.layout.activity_video_preview
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