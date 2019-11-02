package io.github.jsamol.mvitemplate.app.di.component

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import io.github.jsamol.mvitemplate.app.MviApp
import io.github.jsamol.mvitemplate.app.di.module.ActivityModule
import io.github.jsamol.mvitemplate.app.di.module.AppModule
import io.github.jsamol.mvitemplate.app.di.scope.ApplicationScope

@ApplicationScope
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    ActivityModule.BindingModule::class
])
interface AppComponent : AndroidInjector<MviApp> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<MviApp>
}