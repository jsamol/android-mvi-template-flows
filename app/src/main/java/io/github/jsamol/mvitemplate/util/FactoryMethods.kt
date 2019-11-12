package io.github.jsamol.mvitemplate.util

import android.os.Bundle

fun bundle(initBlock: Bundle.() -> Unit): Bundle = Bundle().apply(initBlock)