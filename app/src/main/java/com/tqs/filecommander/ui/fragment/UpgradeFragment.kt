package com.tqs.filecommander.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.tqs.filecommander.R
import com.tqs.filecommander.databinding.FragmentUpgradeBinding
import com.tqs.filecommander.ui.base.BaseFragment
import com.tqs.filecommander.vm.fragment.UpgradeVM

class UpgradeFragment : BaseFragment<FragmentUpgradeBinding, UpgradeVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_upgrade

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpgradeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initData()
        return root
    }

    override fun initData() {
        viewModel = ViewModelProvider(this).get(UpgradeVM::class.java)
        val textView: TextView = binding.textHome
        viewModel.title.observe(viewLifecycleOwner) {
            textView.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}