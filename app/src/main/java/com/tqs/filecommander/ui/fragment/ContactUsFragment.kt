package com.tqs.filecommander.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tqs.filecommander.R
import com.tqs.filecommander.databinding.FragmentContactUsBinding
import com.tqs.filecommander.ui.base.BaseFragment
import com.tqs.filecommander.vm.fragment.ContactUsVM

class ContactUsFragment : BaseFragment<FragmentContactUsBinding, ContactUsVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_contact_us

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentContactUsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initData()
        return root
    }

    override fun initData() {
        viewModel =
            ViewModelProvider(this).get(ContactUsVM::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}