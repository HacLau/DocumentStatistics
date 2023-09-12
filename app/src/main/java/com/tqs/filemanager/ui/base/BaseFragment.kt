package com.tqs.filemanager.ui.base

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tqs.document.statistics.databinding.FragmentUpgradeBinding
import com.tqs.filemanager.vm.fragment.UpgradeVM

abstract class BaseFragment<VB: ViewDataBinding,VM:ViewModel>: Fragment()  {
    protected lateinit var binding: VB
    protected lateinit var viewModel: VM
    abstract val layoutId:Int
    private var REQUEST_CODE_PERMISSION = 0x00099

    abstract fun initData()

    open fun requestPermission(
        permissions: Array<String>,
        requestCode: Int
    ) {
        REQUEST_CODE_PERMISSION = requestCode
        if (checkPermissions(permissions)) {
            permissionSuccess(REQUEST_CODE_PERMISSION)
        } else {
            try {
                val needPermissions =
                    getDeniedPermissions(permissions)
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    needPermissions.toTypedArray(),
                    REQUEST_CODE_PERMISSION
                )
            } catch (e: Exception) {
                Log.e("BaseActivity", "获取权限失败 Exception = $e")
            }
        }
    }

    fun checkPermissions(permissions: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    fun getDeniedPermissions(permissions: Array<String>): List<String> {
        val needRequestPermissionList: MutableList<String> =
            ArrayList()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(requireActivity(), permission) !=
                PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)
            ) {
                needRequestPermissionList.add(permission)
            }
        }
        return needRequestPermissionList
    }

    /**
     * 系统请求权限回调
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (verifyPermissions(grantResults)) {
                permissionSuccess(REQUEST_CODE_PERMISSION)
            } else {
                permissionFail(REQUEST_CODE_PERMISSION)
            }
        }
    }

    private fun verifyPermissions(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    open fun permissionSuccess(requestCode: Int) {
    }
    open fun permissionFail(requestCode: Int) {
    }
}