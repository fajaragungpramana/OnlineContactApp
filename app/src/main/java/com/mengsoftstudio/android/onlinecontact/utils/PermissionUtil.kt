package com.mengsoftstudio.android.onlinecontact.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat

object PermissionUtil {

    fun isExternalStorageGranted(context: Context?): Boolean {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if(ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                return false

        return true
    }

    fun isPhoneCallGranted(context: Context?): Boolean {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if(ContextCompat.checkSelfPermission(context!!, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                return false

        return true
    }

}