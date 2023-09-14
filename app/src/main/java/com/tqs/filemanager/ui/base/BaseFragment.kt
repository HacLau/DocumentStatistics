package com.tqs.filemanager.ui.base

import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseFragment<VB : ViewDataBinding, VM : ViewModel> : Fragment() {
    protected lateinit var binding: VB
    protected lateinit var viewModel: VM
    abstract val layoutId: Int
    public var REQUEST_CODE_PERMISSION = 0x00099

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
                Log.e("BaseFragment", "Exception = $e")
            }
        }
    }

    private fun checkPermissions(permissions: Array<String>): Boolean {
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

    private fun getDeniedPermissions(permissions: Array<String>): List<String> {
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