package com.tqs.filemanager.ui.fragment

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.FragmentFileManagerBinding
import com.tqs.filemanager.ui.activity.audio.AudioListActivity
import com.tqs.filemanager.ui.activity.documents.DocumentsListActivity
import com.tqs.filemanager.ui.activity.download.DownloadListActivity
import com.tqs.filemanager.ui.activity.image.ImageListActivity
import com.tqs.filemanager.ui.activity.video.VideoListActivity
import com.tqs.filemanager.ui.base.BaseFragment
import com.tqs.filemanager.vm.fragment.FileManagerVM

class FileManagerFragment : BaseFragment<FragmentFileManagerBinding,FileManagerVM>(),
    View.OnClickListener {
    override val layoutId: Int
        get() = R.layout.fragment_file_manager

    private var totalSpace :Long = 100L
    private var usedSpace :Long = 100L
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
        viewModel = ViewModelProvider(this).get(FileManagerVM::class.java)
        viewModel.progressValue.observe(requireActivity()){
            binding.mainProgressBar.progress = (it * 1.0f / totalSpace * 100).toInt()
            binding.tvSpaceProportion.text = Html.fromHtml("<font color='#FF4F15'>${usedSpace / 1024 / 1024}</font> / ${totalSpace / 1024 / 1024}  GB")
        }
        getMemoryInfo()
        binding.clFileManagerAudio.setOnClickListener(this)
        binding.clFileManagerDocuments.setOnClickListener(this)
        binding.clFileManagerDownload.setOnClickListener(this)
        binding.clFileManagerImage.setOnClickListener(this)
        binding.clFileManagerVideo.setOnClickListener(this)
    }

    private fun getMemoryInfo() {
        val am :ActivityManager = requireActivity().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        am.getMemoryInfo(memoryInfo)
        usedSpace = memoryInfo.totalMem - memoryInfo.availMem
        totalSpace = memoryInfo.totalMem
        viewModel.progressValue.value = usedSpace

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.cl_file_manager_image ->{
                jumpImageListActivity()
            }
            R.id.cl_file_manager_documents ->{
                jumpDocumentsListActivity()
            }
            R.id.cl_file_manager_audio->{
                jumpAudioListActivity()
            }
            R.id.cl_file_manager_video->{
                jumpVideoListActivity()
            }
            R.id.cl_file_manager_download ->{
                jumpDownloadListActivity()
            }

        }
    }


    private fun jumpImageListActivity() {
        val intent = Intent(requireActivity(), ImageListActivity::class.java)
        startActivity(intent)
    }
    private fun jumpDocumentsListActivity() {
        val intent = Intent(requireActivity(), DocumentsListActivity::class.java)
        startActivity(intent)
    }

    private fun jumpAudioListActivity() {
        val intent = Intent(requireActivity(), AudioListActivity::class.java)
        startActivity(intent)
    }

    private fun jumpVideoListActivity() {
        val intent = Intent(requireActivity(), VideoListActivity::class.java)
        startActivity(intent)
    }

    private fun jumpDownloadListActivity() {
        val intent = Intent(requireActivity(), DownloadListActivity::class.java)
        startActivity(intent)
    }
}