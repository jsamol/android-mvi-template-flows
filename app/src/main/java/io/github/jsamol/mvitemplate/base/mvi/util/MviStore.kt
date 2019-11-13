package io.github.jsamol.mvitemplate.base.mvi.util

import io.github.jsamol.mvitemplate.base.mvi.model.MviAction
import io.github.jsamol.mvitemplate.base.mvi.model.MviActionResult
import io.github.jsamol.mvitemplate.base.mvi.model.MviViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
abstract class MviStore<VS : MviViewState, A : MviAction, AR : MviActionResult,
                        D : MviDispatcher<A, AR>, R : MviReducer<VS, AR>> {

    @Inject
    lateinit var dispatcher: D

    @Inject
    lateinit var reducer: R

    protected abstract val initViewState: VS

    private var _viewState: VS? = null
    private var viewState: VS
        get() = _viewState ?: initViewState
        set(value) { _viewState = value }

    private val actionChannel: BroadcastChannel<A> = BroadcastChannel(Channel.BUFFERED)

    val viewStateFlow: Flow<VS> by lazy {
        actionChannel
            .asFlow()
            .logDebug("Action")
            .flatMapConcat { dispatcher.dispatch(it) }
            .logDebug("ActionResult")
            .scan(viewState) { viewState, actionResult -> reducer.reduce(viewState, actionResult) }
            .debounce(DEBOUNCE_VIEW_STATE)
            .distinctUntilChanged()
            .logDebug("ViewState")
            .onEach { viewState = it }
            .flowOn(Dispatchers.Default)
    }

    suspend fun intent(action: A) {
        actionChannel.send(action)
    }

    private fun <T> Flow<T>.logDebug(tag: String): Flow<T> =
        onEach { Timber.d("$tag: ${it.toString()}") }

    companion object {
        private const val DEBOUNCE_VIEW_STATE = 250L
    }
}