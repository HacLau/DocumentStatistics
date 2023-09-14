package com.tqs.filemanager.ui.activity

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.Gravity
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivityImagePreviewBinding
import com.tqs.filemanager.model.FileEntity
import com.tqs.filemanager.ui.adapter.PreviewAdapter
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.ui.view.ConfirmAndCancelDialog
import com.tqs.filemanager.ui.view.FileDetailPopupWindow
import com.tqs.filemanager.vm.activity.PreviewVM
import com.tqs.filemanager.vm.utils.Common

class PreviewActivity : BaseActivity<ActivityImagePreviewBinding, PreviewVM>(){
    override val layoutId: Int
        get() = R.layout.activity_image_preview
    override val TAG: String
        get() = this.packageName
    private var previewMediaList: MutableList<FileEntity>? = null
    private var currentIndex = 0

    private var mDialog: ConfirmAndCancelDialog? = null
    private var mPopupWindow: FileDetailPopupWindow? = null
    private var mPreviewAdapter: PreviewAdapter? = null
    private var mPageType: String = Common.IMAGE_LIST

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this)[PreviewVM::class.java]
        binding.titleBar.setLeftClickListener {
            finish()
        }
        currentIndex = intent.getIntExtra("selectImageIndex", 0)
        object : TypeToken<ArrayList<String>>() {}.type
        previewMediaList = Gson().fromJson(
            intent.getStringExtra("previewFileList"),
            object : TypeToken<ArrayList<FileEntity>>() {}.type
        ) as MutableList<FileEntity>
        Log.e(TAG, "currentIndex = $currentIndex list = ${previewMediaList?.size}")
        mPageType = intent.getStringExtra(Common.PAGE_TYPE).toString()
        setTitleText()
        mPreviewAdapter = PreviewAdapter(this, previewMediaList)
        binding.vpShowMedia.adapter = mPreviewAdapter
        binding.vpShowMedia.offscreenPageLimit = 0
        binding.vpShowMedia.currentItem = currentIndex
        binding.vpShowMedia.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                currentIndex = position
                if (Common.VIDEO_LIST == mPageType) {
                    stopPlayer()
                }
                setTitleText()
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
        binding.ivWarningImage.setOnClickListener {
            setPopupWindow()
        }
        binding.ivDeleteImage.setOnClickListener {
            setDialogConfirmAndCancel()
        }
        binding.ivShareImage.setOnClickListener {
            setPopupWindow()
        }

        mPreviewAdapter?.setOnClickPlayListener(object : PreviewAdapter.OnClickPlayListener {
            override fun playVideo(surfaceView: SurfaceView, position: Int) {
            }
        })

    }

    private fun stopPlayer() {
        mPreviewAdapter?.stopPlayer()
    }


    private fun setDialogConfirmAndCancel() {
        if (mDialog == null) {
            mDialog = ConfirmAndCancelDialog(this)
            mDialog?.setCancelClickListener {
                mDialog?.dismiss()
            }
            mDialog?.setSureClickListener {
                mDialog?.dismiss()
                //todo delete file
            }
        }
        mDialog?.show()
    }

    private fun setPopupWindow() {
        if (mPopupWindow == null) {
            mPopupWindow = FileDetailPopupWindow(this)
            previewMediaList?.let { mPopupWindow?.setFileInfo(it[currentIndex]) }
        }
        mPopupWindow?.showAsDropDown(binding.titleBar, 0, 0, Gravity.CENTER)
    }

    private fun setTitleText() {
        binding.titleBar.setTitleText("${currentIndex + 1} / ${previewMediaList!!.size}")
    }

}