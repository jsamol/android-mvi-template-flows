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
import java.util.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
abstract class MviStore<VS : MviViewState, A : MviAction, AR : MviActionResult,
                        D : MviDispatcher<A, AR>, R : MviReducer<VS, AR>>() {

    @Inject
    lateinit var dispatcher: D

    @Inject
    lateinit var reducer: R

    protected abstract val initViewState: VS

    private var _viewState: VS? = null
    private var viewState: VS
        get() = _viewState ?: initViewState
        set(value) { _viewState = value }

    private var initActions: MutableList<A>? = mutableListOf()
    private var hasStateObservers: Boolean
        get() = initActions == null
        set(value) {
            initActions = if (value) null else mutableListOf()
        }

    private val actionChannel: BroadcastChannel<A> = BroadcastChannel(Channel.BUFFERED)

    val viewStateFlow: Flow<VS> by lazy {
        actionChannel
            .asFlow()
            .debounce(DEBOUNCE_VIEW_STATE)
            .onStart { emitAll(initActions).also { hasStateObservers = true } }
            .logDebug("Action")
            .flatMapConcat { dispatcher.dispatch(it) }
            .logDebug("ActionResult")
            .scan(viewState) { viewState, actionResult -> reducer.reduce(viewState, actionResult) }
            .distinctUntilChanged()
            .logDebug("ViewState")
            .onEach { viewState = it }
            .flowOn(Dispatchers.Default)
            .onCompletion { hasStateObservers = false }
    }

    suspend fun intent(action: A) {
        if (hasStateObservers) {
            actionChannel.send(action)
        } else {
            initActions?.add(action)
        }
    }

    private suspend fun <T> FlowCollector<T>.emitAll(list: List<T>?) {
        list?.let { emitAll(it.asFlow()) }
    }

    private fun <T> Flow<T>.logDebug(tag: String): Flow<T> =
        onEach { Timber.d("$tag: ${it.toString()}") }

    companion object {
        private const val DEBOUNCE_VIEW_STATE = 250L
    }
}