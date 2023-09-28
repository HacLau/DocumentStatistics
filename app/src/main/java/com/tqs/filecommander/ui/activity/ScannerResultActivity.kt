package com.tqs.filecommander.ui.activity

import android.text.Html
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tqs.filecommander.R
import com.tqs.filecommander.databinding.ActivityScannerResultBinding
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.base.BaseAds
import com.tqs.filecommander.model.DocumentEntity
import com.tqs.filecommander.model.FileEntity
import com.tqs.filecommander.base.BaseActivity
import com.tqs.filecommander.vm.activity.DocListVM
import com.tqs.filecommander.utils.Common
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScannerResultActivity : BaseActivity<ActivityScannerResultBinding, DocListVM>() {
    override val layoutId: Int
        get() = R.layout.activity_scanner_result
    override val TAG: String
        get() = this.packageName
    private var mDataList: ArrayList<DocumentEntity> = arrayListOf()

    private var mPageType: String = ""
    private var baseAds: BaseAds? = null
    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        mPageType = intent.getStringExtra(Common.PAGE_TYPE).toString()
        viewModel = ViewModelProvider(this)[DocListVM::class.java]
        binding.titleBar.setLeftClickListener {
            finish()
        }
        viewModel.dataList.observe(this) {
            statisticsListDirectory(it)
            if (it.size > 0) {
                statisticsFileType(it)
            }
        }
        getDocDataList()
        binding.vFileOk.setOnClickListener {
            jumpMediaListActivity(mPageType)
        }
    }

    override fun onResume() {
        super.onResume()
        AdsManager.adsNativeMain.withLoad(this) {
            if (AdsManager.adsNativeMain.isCacheNotEmpty) {
                baseAds?.destroyNative()
                kotlin.runCatching {
                    AdsManager.adsNativeMain.showNativeAds(this@ScannerResultActivity, binding.nativeFrame) {
                        baseAds = it
                        Log.e(TAG, "native is show")
                    }
                }
            }
        }


    }

    private fun getDocDataList() {
        when (intent.getStringExtra(Common.PAGE_TYPE)) {
            Common.IMAGE_LIST -> {
                viewModel.getImageList(this)
                binding.titleBar.setTitleText("Images")
                binding.tvFileTitle.text = "Images"
            }

            Common.VIDEO_LIST -> {
                viewModel.getVideoList(this)
                binding.titleBar.setTitleText("Video")
                binding.tvFileTitle.text = "Video"
            }

            Common.AUDIO_LIST -> {
                viewModel.getAudioList(this)
                binding.titleBar.setTitleText("Audio")
                binding.tvFileTitle.text = "Audio"
            }

            Common.DOCUMENTS_LIST -> {
                viewModel.getDocumentsList(this)
                binding.titleBar.setTitleText("Documents")
                binding.tvFileTitle.text = "Documents"
            }

            Common.DOWNLOAD_LIST -> {
                viewModel.getDownloadList(this)
                binding.titleBar.setTitleText("Download")
                binding.tvFileTitle.text = "Download"
            }
        }
    }


    private fun statisticsFileType(fileEntities: ArrayList<FileEntity>) {
        val hashMap = HashMap<String, DocumentEntity>()
        for (file in fileEntities) {
            file.path?.let {
                val suffix = it.substring(it.lastIndexOf(".", it.length))
                var document = hashMap[suffix]
                if (document == null) {
                    document = DocumentEntity(suffix = suffix, number = 1)
                } else {
                    document.number = document.number + 1
                }
                document.typeFile = file.fileType
                document.path = file.path!!
                hashMap.put(suffix, document)
            }
        }
        mDataList = ArrayList<DocumentEntity>(hashMap.values)
    }

    private fun statisticsListDirectory(fileEntities: ArrayList<FileEntity>) {
        val hashSet = HashSet<String>()
        for (file in fileEntities) {
            file.path?.let {
                val dir = it.substring(0, it.lastIndexOf("/"))
                hashSet.add(dir)
            }
        }
        binding.tvFileDescription.text =
            Html.fromHtml("<font color='#000000'><big><big>${hashSet.size}</big></big></font>  folders and <font color='#000000'><big><big>${fileEntities.size}</big></big></font> files")
    }

    override fun onDestroy() {
        super.onDestroy()
        baseAds?.destroyNative()
    }

}