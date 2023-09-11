package com.tqs.filemanager.ui.activity

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivityImagePreviewBinding
import com.tqs.filemanager.model.FileEntity
import com.tqs.filemanager.ui.adapter.PreviewAdapter
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.vm.activity.PreviewVM

class PreviewActivity : BaseActivity<ActivityImagePreviewBinding, PreviewVM>() {
    override val layoutId: Int
        get() = R.layout.activity_image_preview
    override val TAG: String
        get() = this.packageName
    private var previewMediaList:MutableList<FileEntity>? = null
    private var currentIndex = 0
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this)[PreviewVM::class.java]
        binding.titleBar.setLeftClickListener {
            finish()
        }
        currentIndex = intent.getIntExtra("selectImageIndex",0)
        object :TypeToken<ArrayList<String>>(){}.type
        previewMediaList = Gson().fromJson(intent.getStringExtra("previewFileList"),object :TypeToken<ArrayList<FileEntity>>(){}.type) as MutableList<FileEntity>
        Log.e(TAG,"currentIndex = $currentIndex list = ${previewMediaList?.size}")
        setTitleText()
        binding.vpShowMedia.adapter = PreviewAdapter(this,previewMediaList)
        binding.vpShowMedia.offscreenPageLimit = 3
        binding.vpShowMedia.currentItem = currentIndex
        binding.vpShowMedia.addOnPageChangeListener(object :OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                currentIndex = position
                setTitleText()
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }

    private fun setTitleText() {
        binding.titleBar.setTitleText("${currentIndex + 1} / ${previewMediaList!!.size}")
    }
}