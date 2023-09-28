package com.tqs.filecommander.ui.activity

import android.text.Html
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import androidx.lifecycle.ViewModelProvider
import com.tqs.filecommander.R
import com.tqs.filecommander.databinding.ActivityDocListBinding
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.model.DocumentEntity
import com.tqs.filecommander.model.FileEntity
import com.tqs.filecommander.adapter.DocAdapter
import com.tqs.filecommander.base.BaseActivity
import com.tqs.filecommander.ui.view.ConfirmAndCancelDialog
import com.tqs.filecommander.utils.Common
import com.tqs.filecommander.utils.FileUtils
import com.tqs.filecommander.vm.MainVM

class DocListActivity : BaseActivity<ActivityDocListBinding, MainVM>() {
    override val layoutId: Int
        get() = R.layout.activity_doc_list
    override val TAG: String
        get() = this.packageName
    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this)[MainVM::class.java]
        binding.titleBar.setLeftClickListener {
            finish()
        }
        getDocDataList()
        viewModel.dataList.observe(this) {
            statisticsListDirectory(it)
            if (it.size > 0) {
                statisticsFileType(it)
            }
        }
        viewModel.listSelectCount.observe(this) {
            when (it > 0) {
                true -> {
                    binding.vFileDelete.setBackgroundResource(R.drawable.bg_delete_selected)
                }

                false -> {
                    binding.vFileDelete.setBackgroundResource(R.drawable.bg_delete_normal)
                }
            }
        }
        viewModel.mDocAdapter = DocAdapter(this, viewModel.docDataList)
        binding.lvFileList.adapter = viewModel.mDocAdapter
        binding.lvFileList.onItemLongClickListener = OnItemLongClickListener { _, _, position, _ ->
            viewModel.mDocAdapter.setSelected(true)
            viewModel.docDataList[position].selected = true
            viewModel.mDocAdapter.setData(viewModel.docDataList)
            viewModel.listSelectCount.value = viewModel.listSelectCount.value?.plus(1)
            true
        }

        binding.lvFileList.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            if (viewModel.mDocAdapter.getSelected()) {
                if (viewModel.docDataList[position].selected) {
                    viewModel.listSelectCount.value = viewModel.listSelectCount.value?.minus(1)
                } else {
                    viewModel.listSelectCount.value = viewModel.listSelectCount.value?.plus(1)
                }
                viewModel.docDataList[position].selected = !viewModel.docDataList[position].selected
                viewModel.mDocAdapter.setData(viewModel.docDataList)
            }
        }
        binding.vFileDelete.setOnClickListener {
            AdsManager.adsInsertResultClean.showFullScreenAds(this@DocListActivity) {
                showDeleteDialog()
            }
        }
    }

    private fun getDocDataList() {
        when (intent.getStringExtra(Common.PAGE_TYPE)) {
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

    private fun showDeleteDialog() {
        if (viewModel.mDeleteDialog == null) {
            viewModel.mDeleteDialog = ConfirmAndCancelDialog(this, {
                viewModel.mDeleteDialog?.dismiss()
            }, {
                viewModel.mDeleteDialog?.dismiss()
                deleteSelectedDoc()
                getDocDataList()
            })
        }
        viewModel.mDeleteDialog?.show()
    }

    private fun deleteSelectedDoc() {
        for (doc in viewModel.docDataList) {
            if (doc.selected) {
                for (file in viewModel.dataList.value!!) {
                    if (file.path?.endsWith(doc.suffix) == true) {
                        FileUtils.deleteFile(file.path!!)
                    }
                }
            }
        }
        viewModel.mDocAdapter.setSelected(false)
        viewModel.listSelectCount.value = 0
        setResult(RESULT_OK)
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
        viewModel.docDataList = ArrayList<DocumentEntity>(hashMap.values)
        viewModel.mDocAdapter.setData(viewModel.docDataList)
    }

    private fun statisticsListDirectory(fileEntities: ArrayList<FileEntity>) {
        val hashSet = HashSet<String>()
        for (file in fileEntities) {
            file.path?.let {
                val dir = it.substring(0, it.lastIndexOf("/"))
                hashSet.add(dir)
                Log.e(TAG, dir)
            }
        }
        binding.tvFileDescription.text =
            Html.fromHtml("<font color='#000000'><big><big>${hashSet.size}</big></big></font>  folders and <font color='#000000'><big><big>${fileEntities.size}</big></big></font> files")
    }

    override fun onBackPressed() {
        if (viewModel.listSelectCount.value!! > 0 || viewModel.mDocAdapter.getSelected()) {
            for (doc in viewModel.docDataList) {
                doc.selected = false
            }
            viewModel.mDocAdapter.setSelected(false)
            viewModel.mDocAdapter.setData(viewModel.docDataList)
            viewModel.listSelectCount.value = 0
        } else {
            super.onBackPressed()
        }
    }
}