package io.github.jsamol.mvitemplate.app

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.github.jsamol.mvitemplate.BuildConfig
import io.github.jsamol.mvitemplate.app.di.component.DaggerAppComponent
import timber.log.Timber

class MviApp : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()

        initTimber()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(this)

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}