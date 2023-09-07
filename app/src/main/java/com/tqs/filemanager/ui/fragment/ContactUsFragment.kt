package com.tqs.filemanager.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.FragmentContactUsBinding
import com.tqs.filemanager.ui.base.BaseFragment
import com.tqs.filemanager.vm.fragment.ContactUsVM

class ContactUsFragment : BaseFragment<FragmentContactUsBinding,ContactUsVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_contact_us

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(ContactUsVM::class.java)

        binding = FragmentContactUsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initData()
        return root
    }

    override fun initData() {
        viewModel =
            ViewModelProvider(this).get(ContactUsVM::class.java)

        val textView: TextView = binding.textSlideshow
        viewModel.title.observe(viewLifecycleOwner) {
            textView.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}