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
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.base.BaseActivity
import com.tqs.filecommander.ui.view.ConfirmAndCancelDialog
import com.tqs.filecommander.ui.view.FileDetailPopupWindow
import com.tqs.filecommander.utils.Common
import com.tqs.filecommander.utils.FileUtils
import com.tqs.filecommander.vm.MainVM
import java.io.File

class PreviewActivity : BaseActivity<ActivityImagePreviewBinding, MainVM>() {
    override val layoutId: Int
        get() = R.layout.activity_image_preview
    override val TAG: String
        get() = this.packageName

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this)[MainVM::class.java]
        binding.titleBar.setLeftClickListener {
            setResult()
            finish()
        }
        viewModel.currentIndex = intent.getIntExtra("selectImageIndex", 0)
        object : TypeToken<ArrayList<String>>() {}.type
        viewModel.previewMediaList = Gson().fromJson(
            intent.getStringExtra("previewFileList"),
            object : TypeToken<ArrayList<FileEntity>>() {}.type
        ) as MutableList<FileEntity>
        viewModel.mPageType = intent.getStringExtra(Common.PAGE_TYPE).toString()
        setTitleText()
        viewModel.mPreviewAdapter = PreviewAdapter(this, viewModel.previewMediaList) {position ->
            if (binding.clMenu.isVisible)
                binding.clMenu.visibility = View.GONE
            else
                binding.clMenu.visibility = View.VISIBLE
        }
        binding.vpShowMedia.adapter = viewModel.mPreviewAdapter
        binding.vpShowMedia.offscreenPageLimit = 0
        binding.vpShowMedia.currentItem = viewModel.currentIndex
        binding.vpShowMedia.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                viewModel.currentIndex = position
                if (Common.VIDEO_LIST == viewModel.mPageType) {
                    stopPlayer()
                }
                setTitleText()
                binding.clMenu.visibility = View.GONE
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
        binding.ivWarningImage.setOnClickListener {
            AdsManager.adsInsertResultClean.showFullScreenAds(this@PreviewActivity) {
                setPopupWindow()
            }
        }
        binding.ivDeleteImage.setOnClickListener {
            AdsManager.adsInsertResultClean.showFullScreenAds(this@PreviewActivity){
                setDialogConfirmAndCancel()
            }
        }
        binding.ivShareImage.setOnClickListener {
            AdsManager.adsInsertResultScan.showFullScreenAds(this@PreviewActivity) {
                setSharedFile()
            }
        }
    }

    private fun setSharedFile() {
        val uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", File(viewModel.previewMediaList?.get(viewModel.currentIndex)?.path ?: ""))
        val type = viewModel.previewMediaList?.get(viewModel.currentIndex)?.mimeType

        startActivity(createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            setType(type)
        }, "shared  file to:"))
    }

    private fun stopPlayer() {
        viewModel.mPreviewAdapter.stopPlayer()
    }


    private fun setDialogConfirmAndCancel() {
        if (viewModel.mDeleteDialog == null) {
            viewModel.mDeleteDialog = ConfirmAndCancelDialog(this, {
                viewModel.mDeleteDialog?.dismiss()
            }, {
                viewModel.mDeleteDialog?.dismiss()
                if (FileUtils.deleteFile(viewModel.previewMediaList!![viewModel.currentIndex].path!!)) {
                    viewModel.deletedFile = true
                    viewModel.previewMediaList!!.remove(viewModel.previewMediaList?.get(viewModel.currentIndex))
                    viewModel.mPreviewAdapter.setData(viewModel.previewMediaList!!)
                    viewModel.mPreviewAdapter.destroyItem(binding.vpShowMedia, -1, binding.vpShowMedia.rootView)
                    viewModel.mPreviewAdapter.notifyDataSetChanged()
                    setTitleText()
                    setResult()
                }
            })

        }
        viewModel.mDeleteDialog?.show()
    }

    private fun setPopupWindow() {
        if (viewModel.mPopupWindow == null) {
            viewModel.mPopupWindow = FileDetailPopupWindow(this)
            viewModel.previewMediaList?.let { viewModel.mPopupWindow?.setFileInfo(it[viewModel.currentIndex]) }
        }
        viewModel.mPopupWindow?.showAsDropDown(binding.titleBar, 0, 0, Gravity.CENTER)
    }

    private fun setTitleText() {
        binding.titleBar.setTitleText("${viewModel.currentIndex + 1} / ${viewModel.previewMediaList!!.size}")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult()
    }

    private fun setResult() {
        if (viewModel.deletedFile) {
            setResult(RESULT_OK, Intent().apply {
                putExtra("currentIndex", viewModel.currentIndex)
                putExtra("deleteResult", true)
            })
        } else {
            setResult(RESULT_CANCELED, Intent().apply {
            })
        }

    }
}