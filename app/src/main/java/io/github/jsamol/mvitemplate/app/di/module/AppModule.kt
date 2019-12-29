package io.github.jsamol.mvitemplate.app.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import io.github.jsamol.mvitemplate.app.MviApp
import io.github.jsamol.mvitemplate.app.di.qualifier.ApplicationContext
import io.github.jsamol.mvitemplate.app.di.scope.ApplicationScope

@Module
object AppModule {

    @ApplicationScope
    @Provides
    @ApplicationContext
    fun provideApplicationContext(application: MviApp): Context =
        application.applicationContext

    @ApplicationScope
    @Provides
    fun provideSharedPreferences(application: MviApp): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(application)
}