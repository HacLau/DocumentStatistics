package com.tqs.filecommander.ui.activity

import android.content.Intent
import android.content.Intent.createChooser
import android.os.Build
import android.view.Gravity
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tqs.filecommander.R
import com.tqs.filecommander.databinding.ActivityImagePreviewBinding
import com.tqs.filecommander.model.FileEntity
import com.tqs.filecommander.adapter.PreviewAdapter
import com.tqs.filecommander.ui.base.BaseActivity
import com.tqs.filecommander.ui.view.ConfirmAndCancelDialog
import com.tqs.filecommander.ui.view.FileDetailPopupWindow
import com.tqs.filecommander.vm.activity.PreviewVM
import com.tqs.filecommander.utils.Common
import com.tqs.filecommander.utils.FileUtils
import java.io.File

class PreviewActivity : BaseActivity<ActivityImagePreviewBinding, PreviewVM>() {
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
    private var deletedFile: Boolean = false

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this)[PreviewVM::class.java]
        binding.titleBar.setLeftClickListener {
            setResult()
            finish()
        }
        currentIndex = intent.getIntExtra("selectImageIndex", 0)
        object : TypeToken<ArrayList<String>>() {}.type
        previewMediaList = Gson().fromJson(
            intent.getStringExtra("previewFileList"),
            object : TypeToken<ArrayList<FileEntity>>() {}.type
        ) as MutableList<FileEntity>
        mPageType = intent.getStringExtra(Common.PAGE_TYPE).toString()
        setTitleText()
        mPreviewAdapter = PreviewAdapter(this, previewMediaList) {position ->
            if (binding.clMenu.isVisible)
                binding.clMenu.visibility = View.GONE
            else
                binding.clMenu.visibility = View.VISIBLE
        }
        binding.vpShowMedia.adapter = mPreviewAdapter
        binding.vpShowMedia.offscreenPageLimit = 0
        binding.vpShowMedia.currentItem = currentIndex
        binding.vpShowMedia.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                currentIndex = position
                if (Common.VIDEO_LIST == mPageType) {
                    stopPlayer()
                }
                setTitleText()
                binding.clMenu.visibility = View.GONE
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
        binding.ivWarningImage.setOnClickListener {
//            AdsManager.adsFullScreen.showFullScreenAds(this@PreviewActivity) {
                setPopupWindow()
//            }
        }
        binding.ivDeleteImage.setOnClickListener {
//            AdsManager.adsInsertResultClean.showFullScreenAds(this@PreviewActivity){
                setDialogConfirmAndCancel()
//            }
        }
        binding.ivShareImage.setOnClickListener {
//            AdsManager.adsInsertResultScan.showFullScreenAds(this@PreviewActivity) {
                setSharedFile()
//            }
        }
    }

    private fun setSharedFile() {
        val uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", File(previewMediaList?.get(currentIndex)?.path ?: ""))
        val type = previewMediaList?.get(currentIndex)?.mimeType

        startActivity(createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            setType(type)
        }, "shared  file to:"))
    }

    private fun stopPlayer() {
        mPreviewAdapter?.stopPlayer()
    }


    private fun setDialogConfirmAndCancel() {
        if (mDialog == null) {
            mDialog = ConfirmAndCancelDialog(this, {
                mDialog?.dismiss()
            }, {
                mDialog?.dismiss()
                if (FileUtils.deleteFile(previewMediaList!![currentIndex].path!!)) {
                    deletedFile = true
                    previewMediaList!!.remove(previewMediaList?.get(currentIndex))
                    mPreviewAdapter?.setData(previewMediaList!!)
                    mPreviewAdapter?.destroyItem(binding.vpShowMedia, -1, binding.vpShowMedia.rootView)
                    mPreviewAdapter?.notifyDataSetChanged()
                    setTitleText()
                    setResult()
                }
            })

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

    override fun onBackPressed() {
        super.onBackPressed()
        setResult()
    }

    private fun setResult() {
        if (deletedFile) {
            setResult(RESULT_OK, Intent().apply {
                putExtra("currentIndex", currentIndex)
                putExtra("deleteResult", true)
            })
        } else {
            setResult(RESULT_CANCELED, Intent().apply {
            })
        }

    }
}