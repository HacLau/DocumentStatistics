package com.tqs.filemanager.ui.activity

import android.text.Html
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import androidx.lifecycle.ViewModelProvider
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivityDocListBinding
import com.tqs.filemanager.model.DocumentEntity
import com.tqs.filemanager.model.FileEntity
import com.tqs.filemanager.ui.adapter.DocAdapter
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.ui.view.ConfirmAndCancelDialog
import com.tqs.filemanager.vm.activity.DocListVM
import com.tqs.filemanager.vm.utils.Common
import com.tqs.filemanager.vm.utils.FileUtils

class DocListActivity : BaseActivity<ActivityDocListBinding, DocListVM>() {
    override val layoutId: Int
        get() = R.layout.activity_doc_list
    override val TAG: String
        get() = this.packageName
    private lateinit var mDocAdapter: DocAdapter
    private var mDataList: ArrayList<DocumentEntity> = arrayListOf()
    private var mDeleteDialog: ConfirmAndCancelDialog? = null
    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this)[DocListVM::class.java]
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
        mDocAdapter = DocAdapter(this, mDataList)
        binding.lvFileList.adapter = mDocAdapter
        binding.lvFileList.onItemLongClickListener = OnItemLongClickListener { _, _, position, _ ->
            mDocAdapter.setSelected(true)
            mDataList[position].selected = true
            mDocAdapter.setData(mDataList)
            viewModel.listSelectCount.value = viewModel.listSelectCount.value?.plus(1)
            true
        }

        binding.lvFileList.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            if (mDocAdapter.getSelected()) {
                if (mDataList[position].selected) {
                    viewModel.listSelectCount.value = viewModel.listSelectCount.value?.minus(1)
                } else {
                    viewModel.listSelectCount.value = viewModel.listSelectCount.value?.plus(1)
                }
                mDataList[position].selected = !mDataList[position].selected
                mDocAdapter.setData(mDataList)
            }
        }
        binding.vFileDelete.setOnClickListener {
            showDeleteDialog()
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
        if (mDeleteDialog == null) {
            mDeleteDialog = ConfirmAndCancelDialog(this, {
                mDeleteDialog?.dismiss()
            }, {
                mDeleteDialog?.dismiss()
                deleteSelectedDoc()
                getDocDataList()
            })
        }
        mDeleteDialog?.show()
    }

    private fun deleteSelectedDoc() {
        for (doc in mDataList) {
            if (doc.selected) {
                for (file in viewModel.dataList.value!!) {
                    if (file.path?.endsWith(doc.suffix) == true) {
                        FileUtils.deleteFile(file.path!!)
                    }
                }
            }
        }
        mDocAdapter.setSelected(false)
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
        mDataList = ArrayList<DocumentEntity>(hashMap.values)
        mDocAdapter.setData(mDataList)
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
        if (viewModel.listSelectCount.value!! > 0 || mDocAdapter.getSelected()) {
            for (doc in mDataList) {
                doc.selected = false
            }
            mDocAdapter.setSelected(false)
            mDocAdapter.setData(mDataList)
            viewModel.listSelectCount.value = 0
        } else {
            super.onBackPressed()
        }
    }
}