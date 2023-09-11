package com.tqs.filemanager.ui.activity

import androidx.lifecycle.ViewModelProvider
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivityDocListBinding
import com.tqs.filemanager.ui.adapter.DocAdapter
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.vm.activity.DocListVM
import com.tqs.filemanager.vm.utils.Common

class DocListActivity : BaseActivity<ActivityDocListBinding, DocListVM>() {
    override val layoutId: Int
        get() = R.layout.activity_doc_list
    override val TAG: String
        get() = this.packageName

    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this)[DocListVM::class.java]
        binding.titleBar.setLeftClickListener {
            finish()
        }
        when(intent.getStringExtra(Common.PAGE_TYPE)){
            Common.AUDIO_LIST ->{
                viewModel.getAudioList(this)
                binding.titleBar.setTitleText("Audio")
                binding.tvFileTitle.text = "Audio"
            }
            Common.DOCUMENTS_LIST ->{
                viewModel.getDocumentsList(this)
                binding.titleBar.setTitleText("Documents")
                binding.tvFileTitle.text = "Documents"
            }

            Common.DOWNLOAD_LIST ->{
                viewModel.getDownloadList(this)
                binding.titleBar.setTitleText("Download")
                binding.tvFileTitle.text = "Download"
            }
        }
        binding.lvFileList.adapter = DocAdapter(this,viewModel.dataList.value!!)
    }
}