package com.tqs.filemanager.ui.fragment

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.FragmentFileManagerBinding
import com.tqs.filemanager.ui.activity.DocListActivity
import com.tqs.filemanager.ui.activity.ImageListActivity
import com.tqs.filemanager.ui.base.BaseFragment
import com.tqs.filemanager.vm.fragment.FileManagerVM
import com.tqs.filemanager.vm.utils.Common
import com.tqs.filemanager.vm.utils.SharedUtils

class FileManagerFragment : BaseFragment<FragmentFileManagerBinding, FileManagerVM>(),
    View.OnClickListener {
    override val layoutId: Int
        get() = R.layout.fragment_file_manager

    private var REQUEST_CODE_PERMISSION = 0x00099
    var permissions = arrayOf<String>(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFileManagerBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initData()
        return root
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[FileManagerVM::class.java]
//        requestPermission(permissions,REQUEST_CODE_PERMISSION)
        setData()
    }

    private fun setData() {
        setProgressValue()
        setMediaList()
        setMediaListSize()
        getMediaInfo()
        setClickListener()
    }

    private fun setProgressValue() {
        viewModel.progressValue.observe(requireActivity()) {
            binding.mainProgressBar.progress = it
            binding.tvSpaceProportion.text =
                Html.fromHtml("<font color='#FF4F15'>${viewModel.availSpace.value}</font> / ${viewModel.totalSpace.value}")
        }
    }

    private fun setMediaList() {
        viewModel.imageList.observe(requireActivity()) {
            viewModel.getImageListSize()
        }
        viewModel.audioList.observe(requireActivity()) {
            viewModel.getAudioListSize()
        }
        viewModel.videoList.observe(requireActivity()) {
            viewModel.getVideoListSize()
        }
        viewModel.documentsList.observe(requireActivity()) {
            viewModel.getDocumentsListSize()
        }
        viewModel.downloadList.observe(requireActivity()) {
            viewModel.getDownloadListSize()
        }
    }

    private fun setMediaListSize() {
        viewModel.imageSpaceSize.observe(requireActivity()) {
            binding.tvImageSpace.text = it
        }
        viewModel.videoSpaceSize.observe(requireActivity()) {
            binding.tvVideoSpace.text = it
        }
        viewModel.audioSpaceSize.observe(requireActivity()) {
            binding.tvAudioSpace.text = it
        }
        viewModel.documentsSpaceSize.observe(requireActivity()) {
            binding.tvDocumentsSpace.text = it
        }
        viewModel.downloadSpaceSize.observe(requireActivity()) {
            binding.tvDownloadSpace.text = it
        }
    }

    private fun getMediaInfo() {
        viewModel.getMemoryInfo()
        viewModel.getImageList(requireContext())
        viewModel.getAudioList(requireContext())
        viewModel.getVideoList(requireContext())
        viewModel.getDocumentsList(requireContext())
        viewModel.getDownloadList(requireContext())
    }

    private fun setClickListener() {
        binding.clFileManagerAudio.setOnClickListener(this)
        binding.clFileManagerDocuments.setOnClickListener(this)
        binding.clFileManagerDownload.setOnClickListener(this)
        binding.clFileManagerImage.setOnClickListener(this)
        binding.clFileManagerVideo.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        if (SharedUtils.getValue(requireContext(),Common.EXTERNAL_STORAGE_PERMISSION,false)?.equals(false) == true){
            requestPermission(permissions, REQUEST_CODE_PERMISSION)
            return
        }
        when (v?.id) {
            R.id.cl_file_manager_image -> {
                jumpImageListActivity()
            }

            R.id.cl_file_manager_documents -> {
                jumpDocumentsListActivity()
            }

            R.id.cl_file_manager_audio -> {
                jumpAudioListActivity()
            }

            R.id.cl_file_manager_video -> {
                jumpVideoListActivity()
            }

            R.id.cl_file_manager_download -> {
                jumpDownloadListActivity()
            }
        }
    }

    private fun jumpImageListActivity() {
        val intent = Intent(requireActivity(), ImageListActivity::class.java)
        intent.putExtra(Common.PAGE_TYPE, Common.IMAGE_LIST)
        startActivity(intent)
    }

    private fun jumpDocumentsListActivity() {
        val intent = Intent(requireActivity(), DocListActivity::class.java)
        intent.putExtra(Common.PAGE_TYPE, Common.DOCUMENTS_LIST)
        startActivity(intent)
    }

    private fun jumpAudioListActivity() {
        val intent = Intent(requireActivity(), DocListActivity::class.java)
        intent.putExtra(Common.PAGE_TYPE, Common.AUDIO_LIST)
        startActivity(intent)
    }

    private fun jumpVideoListActivity() {
        val intent = Intent(requireActivity(), ImageListActivity::class.java)
        intent.putExtra(Common.PAGE_TYPE, Common.VIDEO_LIST)
        startActivity(intent)
    }

    private fun jumpDownloadListActivity() {
        val intent = Intent(requireActivity(), DocListActivity::class.java)
        intent.putExtra(Common.PAGE_TYPE, Common.DOWNLOAD_LIST)
        startActivity(intent)
    }

    override fun permissionSuccess(requestCode: Int) {
        super.permissionSuccess(requestCode)
        if (requestCode == REQUEST_CODE_PERMISSION){
            setData()
        }
    }

    override fun permissionFail(requestCode: Int) {
        super.permissionFail(requestCode)

    }
}