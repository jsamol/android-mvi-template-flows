package io.github.jsamol.mvitemplate.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import dagger.android.support.DaggerAppCompatActivity
import io.github.jsamol.mvitemplate.util.permission.PermissionRequest

abstract class BaseActivity : DaggerAppCompatActivity() {

    @get:LayoutRes
    protected abstract val layoutRes: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)

        initView()
        initListeners()
    }

    protected open fun initView() = Unit
    protected open fun initListeners() = Unit

    protected fun requiresPermissions(
        vararg permissions: String,
        block: () -> Unit = {},
        onDenied: () -> Unit = this::onBackPressed
    ) {

        PermissionRequest.forPermissions(*permissions)
            .onPermissionsGranted(block)
            .onPermissionsDenied(onDenied)
            .observe(this)
    }
}