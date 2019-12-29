package io.github.jsamol.mvitemplate.util.permission

import android.content.pm.PackageManager
import android.os.Bundle
import dagger.android.support.DaggerFragment
import io.github.jsamol.mvitemplate.app.di.qualifier.permission.PermissionArray
import io.github.jsamol.mvitemplate.util.extension.withArguments
import kotlinx.coroutines.CompletableDeferred
import javax.inject.Inject

class PermissionRequestFragment private constructor() : DaggerFragment() {

    @Inject
    @PermissionArray
    lateinit var permissions: Array<out String>

    val grantResultDeferred: CompletableDeferred<Boolean> = CompletableDeferred()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        requestPermissions(permissions, REQUEST_CODE_PERMISSIONS)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            val result = !grantResults.any { it == PackageManager.PERMISSION_DENIED }
            grantResultDeferred.complete(result)

            finish()
        }
    }

    private fun finish() {
        retainInstance = false

        parentFragmentManager
            .beginTransaction()
            .remove(this)
            .commitAllowingStateLoss()
    }

    companion object {
        const val ARGUMENT_KEY_PERMISSIONS = "permissions"

        private const val REQUEST_CODE_PERMISSIONS = 100

        fun getInstance(permissions: List<String>): PermissionRequestFragment =
            PermissionRequestFragment().withArguments {
                putStringArray(ARGUMENT_KEY_PERMISSIONS, permissions.toTypedArray())
            }
    }
}