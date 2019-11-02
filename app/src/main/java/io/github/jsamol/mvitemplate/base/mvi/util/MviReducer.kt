package io.github.jsamol.mvitemplate.base.mvi.util

import io.github.jsamol.mvitemplate.base.mvi.model.MviActionResult
import io.github.jsamol.mvitemplate.base.mvi.model.MviViewState

interface MviReducer<VS : MviViewState, AR : MviActionResult> {
    fun reduce(viewState: VS, actionResult: AR): VS
}