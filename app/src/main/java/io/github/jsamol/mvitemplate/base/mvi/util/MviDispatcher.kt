package io.github.jsamol.mvitemplate.base.mvi.util

import io.github.jsamol.mvitemplate.base.mvi.model.MviAction
import io.github.jsamol.mvitemplate.base.mvi.model.MviActionResult
import kotlinx.coroutines.flow.Flow

interface MviDispatcher<A : MviAction, AR : MviActionResult> {
    fun dispatch(action: A): Flow<AR>
}