package io.github.jsamol.mvitemplate.base.mvi

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import io.github.jsamol.mvitemplate.base.BaseViewModel
import io.github.jsamol.mvitemplate.base.mvi.model.MviAction
import io.github.jsamol.mvitemplate.base.mvi.model.MviViewState
import io.github.jsamol.mvitemplate.base.mvi.util.MviStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseMviViewModel<VS : MviViewState, A : MviAction, S : MviStore<VS, A, *, *, *>> : BaseViewModel() {

    @Inject
    lateinit var store: S

    val viewStateLiveData: LiveData<VS> by lazy { store.viewStateFlow.asLiveData() }

    protected fun intent(action: A) {
        viewModelScope.launch { store.intent(action) }
    }
}