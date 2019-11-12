package io.github.jsamol.mvitemplate.util.extension

import android.os.Bundle
import androidx.fragment.app.Fragment
import io.github.jsamol.mvitemplate.util.bundle

fun <T : Fragment> T.withArguments(argumentsBuilder: Bundle.() -> Unit): T =
    apply { arguments = bundle(argumentsBuilder) }