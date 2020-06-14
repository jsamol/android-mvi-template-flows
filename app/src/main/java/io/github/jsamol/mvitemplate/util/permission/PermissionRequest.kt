package io.github.jsamol.mvitemplate.util.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PermissionRequest private constructor(private val permissions: Array<out String>) {

    private var onGranted: (() -> Unit)? = null
    private var onDenied: (() -> Unit)? = null

    fun onPermissionsGranted(action: () -> Unit): PermissionRequest {
        onGranted = action
        return this
    }

    fun onPermissionsDenied(action: () -> Unit): PermissionRequest {
        onDenied = action
        return this
    }

    fun observe(activity: FragmentActivity) {
        with (activity) {
            observe(this, lifecycleScope, supportFragmentManager)
        }
    }

    fun observe(fragment: Fragment) {
        with (fragment) {
            context?.let { observe(it, lifecycleScope, childFragmentManager) }
        }
    }

    private fun observe(context: Context, coroutineScope: CoroutineScope, fragmentManager: FragmentManager) {
        val allGranted = checkAllGranted(context)

        if (allGranted) {
            onGranted?.invoke()
        } else {
            requestPermissions(context, coroutineScope, fragmentManager)
        }
    }

    private fun checkAllGranted(context: Context): Boolean = permissions.all { context.checkPermissionGranted(it) }

    private fun requestPermissions(context: Context, coroutineScope: CoroutineScope, fragmentManager: FragmentManager) {
        coroutineScope.launch(Dispatchers.Main) {
            val permissionRequestFragment = openPermissionRequestFragment(context, fragmentManager)
            val grantResult = permissionRequestFragment.grantResultDeferred.await()

            if (grantResult) {
                onGranted?.invoke()
            } else {
                onDenied?.invoke()
            }
        }
    }

    private fun openPermissionRequestFragment(context: Context, fragmentManager: FragmentManager): PermissionRequestFragment {
        val permissionRequestFragment =
            PermissionRequestFragment.getInstance(permissions.filterNot { context.checkPermissionGranted(it) })

        fragmentManager
            .beginTransaction()
            .add(permissionRequestFragment, null)
            .commit()

        return permissionRequestFragment
    }

    private fun Context.checkPermissionGranted(permission: String): Boolean =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    companion object {
        fun forPermissions(vararg permissions: String): PermissionRequest = PermissionRequest(permissions)
    }
}