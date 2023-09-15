package com.tqs.filemanager.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.FragmentFileManagerBinding
import com.tqs.filemanager.ui.activity.DocListActivity
import com.tqs.filemanager.ui.activity.ImageListActivity
import com.tqs.filemanager.ui.base.BaseFragment
import com.tqs.filemanager.vm.fragment.FileManagerVM
import com.tqs.filemanager.vm.utils.Common
import com.tqs.filemanager.vm.utils.DateUtils
import com.tqs.filemanager.vm.utils.SharedUtils

class FileManagerFragment : BaseFragment<FragmentFileManagerBinding, FileManagerVM>(),
    View.OnClickListener {
    override val layoutId: Int
        get() = R.layout.fragment_file_manager

    private var REQUEST_CODE_MANAGE_EXTERNAL_STORAGE = 0x00098
    private val startActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            getMediaInfo()
        }
    }
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
        setClickListener()
        viewModel.hadPermission.observe(requireActivity()) {
            if (it)
                setData()
        }
        viewModel.hadPermission.value = false
        getHadPermission()
    }

    private fun getHadPermission() {
        if(SharedUtils.getValue(requireContext(), Common.EXTERNAL_STORAGE_PERMISSION, false) as Boolean
            && (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || SharedUtils.getValue(requireContext(), Common.REQUEST_CODE_MANAGE_EXTERNAL_STORAGE, false) as Boolean)){
            viewModel.hadPermission.value = true
        }
    }

    private fun setData() {
        setProgressValue()
        setMediaList()
        setMediaListSize()
        getMediaInfo()
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
            binding.tvImageTime.text = DateUtils.getCurrentTime()
        }
        viewModel.audioList.observe(requireActivity()) {
            viewModel.getAudioListSize()
            binding.tvAudioTime.text = DateUtils.getCurrentTime()
        }
        viewModel.videoList.observe(requireActivity()) {
            viewModel.getVideoListSize()
            binding.tvVideoTime.text = DateUtils.getCurrentTime()
        }
        viewModel.documentsList.observe(requireActivity()) {
            viewModel.getDocumentsListSize()
            binding.tvDocumentsTime.text = DateUtils.getCurrentTime()
        }
        viewModel.downloadList.observe(requireActivity()) {
            viewModel.getDownloadListSize()
            binding.tvDownloadTime.text = DateUtils.getCurrentTime()
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
        viewModel.getImgListOrderDescByDate(requireContext())
        viewModel.getAudioList(requireContext())
        viewModel.getVideoListOrderDescByDate(requireContext())
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
        if (!judgePermission()) {
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
        startActivityForResult.launch(Intent(requireActivity(), ImageListActivity::class.java).apply {
            putExtra(Common.PAGE_TYPE, Common.IMAGE_LIST)
        })
    }

    private fun jumpDocumentsListActivity() {
        startActivityForResult.launch(Intent(requireActivity(), DocListActivity::class.java).apply {
            putExtra(Common.PAGE_TYPE, Common.DOCUMENTS_LIST)
        })
    }

    private fun jumpAudioListActivity() {
        startActivityForResult.launch(Intent(requireActivity(), DocListActivity::class.java).apply {
            putExtra(Common.PAGE_TYPE, Common.AUDIO_LIST)
        })
    }

    private fun jumpVideoListActivity() {
        startActivityForResult.launch(Intent(requireActivity(), ImageListActivity::class.java).apply {
            putExtra(Common.PAGE_TYPE, Common.VIDEO_LIST)
        })
    }

    private fun jumpDownloadListActivity() {
        startActivityForResult.launch(Intent(requireActivity(), DocListActivity::class.java).apply {
            putExtra(Common.PAGE_TYPE, Common.DOWNLOAD_LIST)
        })
    }

    override fun permissionSuccess(requestCode: Int) {
        super.permissionSuccess(requestCode)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            SharedUtils.putValue(requireContext(), Common.EXTERNAL_STORAGE_PERMISSION, true)
            judgePermission()
        }
    }

    override fun permissionFail(requestCode: Int) {
        super.permissionFail(requestCode)
        Toast.makeText(
            requireContext(),
            "Please allow us access to your directories and files, including photos, videos, and audio files.",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("FileManager", "$resultCode")
        when (requestCode) {
            REQUEST_CODE_MANAGE_EXTERNAL_STORAGE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
                    SharedUtils.putValue(requireContext(), Common.REQUEST_CODE_MANAGE_EXTERNAL_STORAGE, true)
                    judgePermission()
                }
            }
        }
    }

    private fun judgePermission(): Boolean {
        if (!(SharedUtils.getValue(requireContext(), Common.EXTERNAL_STORAGE_PERMISSION, false) as Boolean)) {
            requestPermission(Common.permissions, REQUEST_CODE_PERMISSION)
            return false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()
            && !(SharedUtils.getValue(requireContext(), Common.REQUEST_CODE_MANAGE_EXTERNAL_STORAGE, false) as Boolean)
        ) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = Uri.parse("package:${requireActivity().packageName}")
            startActivityForResult(intent, REQUEST_CODE_MANAGE_EXTERNAL_STORAGE)
            return false
        }
        return true
    }
}