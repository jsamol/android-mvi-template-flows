package io.github.jsamol.mvitemplate.base.mvi

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.github.jsamol.mvitemplate.base.BaseActivity
import io.github.jsamol.mvitemplate.base.BaseViewModel
import io.github.jsamol.mvitemplate.base.mvi.model.MviViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseMviActivity<VM : BaseMviViewModel<VS, *, *>, VS : MviViewState> : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: BaseViewModel.Factory<VM>

    protected abstract val viewModelType: Class<VM>

    protected val viewModel: VM by lazy { ViewModelProvider(this, viewModelFactory)[viewModelType] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!viewModel.isInitialized) {
            initViewModel()
            viewModel.onInitialized()
        }

        viewModel.viewStateLiveData.observe(this, Observer(this::render))
    }

    protected abstract fun render(viewState: VS)

    protected open fun initViewModel() = Unit
}