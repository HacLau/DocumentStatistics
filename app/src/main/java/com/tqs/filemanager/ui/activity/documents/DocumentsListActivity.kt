package com.tqs.filemanager.ui.activity.documents

import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivityDocumentsListBinding
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.vm.activity.documents.DocumentsListVM

class DocumentsListActivity : BaseActivity<ActivityDocumentsListBinding, DocumentsListVM>() {
    override val layoutId: Int
        get() = R.layout.activity_documents_list
    override val TAG: String
        get() = this.packageName

    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        binding.titleBar.setLeftClickListener {
            finish()
        }
    }
}