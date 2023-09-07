package com.tqs.filemanager.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.FragmentContactUsBinding
import com.tqs.document.statistics.databinding.FragmentPrivacyPolicyBinding
import com.tqs.filemanager.ui.base.BaseFragment
import com.tqs.filemanager.vm.fragment.ContactUsVM
import com.tqs.filemanager.vm.fragment.PrivacyPolicyVM

class PrivacyPolicyFragment : BaseFragment<FragmentPrivacyPolicyBinding,PrivacyPolicyVM>(){
    override val layoutId: Int
        get() = R.layout.fragment_privacy_policy
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(PrivacyPolicyVM::class.java)

        binding = FragmentPrivacyPolicyBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initData()
        return root
    }
    override fun initData() {
        viewModel =
            ViewModelProvider(this).get(PrivacyPolicyVM::class.java)

        val textView: TextView = binding.textGallery
        viewModel.title.observe(viewLifecycleOwner) {
            textView.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}