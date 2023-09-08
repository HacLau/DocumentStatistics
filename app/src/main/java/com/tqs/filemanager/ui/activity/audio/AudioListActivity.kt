package com.tqs.filemanager.ui.activity.audio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivityAudioListBinding
import com.tqs.document.statistics.databinding.ActivityMainBinding
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.vm.activity.MainVM

class AudioListActivity : BaseActivity<ActivityAudioListBinding, MainVM>() {
    override val layoutId: Int
        get() = R.layout.activity_audio_list
    override val TAG: String
        get() = this.packageName

    override fun initData() {
    }
}