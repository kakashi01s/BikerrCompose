package com.firefly.bikerr_compose.base

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.firefly.bikerr_compose.utils.NetworkUtils


abstract class BaseActivity : AppCompatActivity() {
    private val mProgressDialog: ProgressDialog? = null

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String?): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission!!) == PackageManager.PERMISSION_GRANTED
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing) {
            mProgressDialog.cancel()
        }
    }

    open fun isNetworkConnected(): Boolean {
        val networkUtils: NetworkUtils?= null
        return networkUtils?.isNetworkConnected(applicationContext)!!
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String?>?, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions!!, requestCode)
        }
    }

    fun openActivity(bundle: Bundle?, calledActivity: Class<*>?) {
        val myIntent = Intent(this, calledActivity)
        myIntent.putExtra("bundle", bundle)
        startActivity(myIntent)
    }
}