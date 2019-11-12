package io.github.jsamol.mvitemplate.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import dagger.android.support.DaggerFragment
import io.github.jsamol.mvitemplate.util.permission.PermissionRequest

abstract class BaseFragment : DaggerFragment() {

    @get:LayoutRes
    protected abstract val layoutRes: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListeners()
    }

    protected open fun initView() = Unit
    protected open fun initListeners() = Unit

    protected fun requiresPermissions(
        vararg permissions: String,
        block: () -> Unit = {},
        onDenied: () -> Unit = { activity?.onBackPressed() }
    ) {

        PermissionRequest.forPermissions(*permissions)
            .onPermissionsGranted(block)
            .onPermissionsDenied(onDenied)
            .observe(this)
    }
}