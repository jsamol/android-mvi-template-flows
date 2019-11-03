package io.github.jsamol.mvitemplate.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

abstract class BaseViewModel : ViewModel() {

    var isInitialized: Boolean = false
        private set

    @CallSuper
    open fun onInitialized() {
        isInitialized = true
    }

    class Factory<VM : ViewModel> @Inject constructor(
        private val viewModelProvider: @JvmSuppressWildcards Provider<VM>
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = viewModelProvider.get() as T
    }
}