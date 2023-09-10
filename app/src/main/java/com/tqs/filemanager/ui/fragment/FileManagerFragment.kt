package com.tqs.filemanager.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Html
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
        setProgressValue()
        setMediaList()
        setMediaListSize()
        getMediaInfo()
        setClickListener()
    }

    private fun setProgressValue() {
        viewModel.progressValue.observe(requireActivity()){
            binding.mainProgressBar.progress = (it * 1.0f / viewModel.totalSpace.value!! * 100).toInt()
            binding.tvSpaceProportion.text = Html.fromHtml("<font color='#FF4F15'>${viewModel.availSpace.value  } GB</font> / ${viewModel.totalSpace.value }  GB")
        }
    }

    private fun setMediaList() {
        viewModel.imageList.observe(requireActivity()){
            viewModel.getImageListSize()
        }
        viewModel.audioList.observe(requireActivity()){
            viewModel.getAudioListSize()
        }
        viewModel.videoList.observe(requireActivity()){
            viewModel.getVideoListSize()
        }
        viewModel.documentsList.observe(requireActivity()){
            viewModel.getDocumentsListSize()
        }
        viewModel.downloadList.observe(requireActivity()){
            viewModel.getDownloadListSize()
        }
    }

    private fun setMediaListSize() {
        viewModel.imageListSize.observe(requireActivity()){
            binding.tvImageSpace.text = "$it MB"
        }
        viewModel.videoListSize.observe(requireActivity()){
            binding.tvVideoSpace.text = "$it MB"
        }
        viewModel.audioListSize.observe(requireActivity()){
            binding.tvAudioSpace.text = "$it MB"
        }
        viewModel.documentsListSize.observe(requireActivity()){
            binding.tvDocumentsSpace.text = "$it MB"
        }
        viewModel.downloadListSize.observe(requireActivity()){
            binding.tvDownloadSpace.text = "$it MB"
        }
    }

    private fun getMediaInfo() {
        viewModel.getMemoryInfo()
        viewModel.getImageList(requireContext())
        viewModel.getAudioList(requireContext())
        viewModel.getVideoList(requireContext())
        viewModel.getDocumentsList(requireContext())
//        viewModel.getDownloadList(requireContext())
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