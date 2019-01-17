package codeliver128.com.mylibrary.utility

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import codeliver128.com.mylibrary.R

private const val PERMISSION_REQUEST_CODE = 1001

open class BaseFragment : androidx.fragment.app.Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        (activity as BaseActivity).loadAdd()
        super.onCreate(savedInstanceState)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        /*if (!(this is QuotesCategoryFragment)) {
            menu!!.clear();
        }*/
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
    }


    fun checkPermission(permissionType: String): Boolean {

        if (Build.VERSION.SDK_INT < 23)
            return true

        if (ContextCompat.checkSelfPermission(activity!!, permissionType) !== PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permissionType)) {
                requestPermissions(arrayOf(permissionType), PERMISSION_REQUEST_CODE)
            } else {
                requestPermissions(arrayOf(permissionType), PERMISSION_REQUEST_CODE)
            }
            return false
        }

        return true
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permissions[0])
            } else {
                onPermissionNotGranted()
            }
        }

    }

    open fun onPermissionGranted(permission: String) {
        throw RuntimeException(activity!!.getResources().getString(R.string.permission_required))
    }

    open fun onPermissionNotGranted() {
        throw RuntimeException(activity!!.getResources().getString(R.string.permission_required))
    }

}
