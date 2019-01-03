package com.example.admin.gallery

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

class PermissionUtil {
    val galleryPermissions = "android.permission.WRITE_EXTERNAL_STORAGE"

    fun verifyPermissions(context: Context, grantResults: Array<String>): Boolean {
        for (result in grantResults) {
            if (ActivityCompat.checkSelfPermission(context, result) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun checkMarshMellowPermission(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }

}
