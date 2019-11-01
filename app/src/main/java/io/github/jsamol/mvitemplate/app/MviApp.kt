package io.github.jsamol.mvitemplate.app

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.github.jsamol.mvitemplate.app.di.component.DaggerAppComponent

class MviApp : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(this)
}