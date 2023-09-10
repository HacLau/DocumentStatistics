package com.tqs.filemanager.ui.activity.audio

import androidx.lifecycle.ViewModelProvider
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivityAudioListBinding
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.vm.activity.MainVM
import com.tqs.filemanager.vm.activity.audio.AudioListVM
import com.tqs.filemanager.vm.activity.image.ImageListVM

class AudioListActivity : BaseActivity<ActivityAudioListBinding, AudioListVM>() {
    override val layoutId: Int
        get() = R.layout.activity_audio_list
    override val TAG: String
        get() = this.packageName

    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this)[AudioListVM::class.java]
        binding.titleBar.setLeftClickListener {
            finish()
        }

    }
}