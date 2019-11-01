package io.github.jsamol.mvitemplate.app.di.module

import android.os.Bundle
import androidx.fragment.app.Fragment
import dagger.Module
import dagger.Provides
import io.github.jsamol.mvitemplate.app.di.qualifier.FragmentBundle

@Module
abstract class FragmentModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        @FragmentBundle
        fun provideArguments(fragment: Fragment): Bundle = fragment.arguments ?: Bundle.EMPTY
    }

    @Module
    abstract class BindingModule
}