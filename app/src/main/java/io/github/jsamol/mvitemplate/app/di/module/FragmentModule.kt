package io.github.jsamol.mvitemplate.app.di.module

import android.os.Bundle
import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import io.github.jsamol.mvitemplate.app.di.qualifier.FragmentBundle
import io.github.jsamol.mvitemplate.app.di.qualifier.permission.PermissionArray
import io.github.jsamol.mvitemplate.app.di.scope.FragmentScope
import io.github.jsamol.mvitemplate.util.permission.PermissionRequestFragment

@Module
abstract class FragmentModule {

    companion object {

        @Provides
        @FragmentBundle
        fun provideArguments(fragment: Fragment): Bundle = fragment.arguments ?: Bundle.EMPTY
    }

    @Module
    abstract class BindingModule {

        @FragmentScope
        @ContributesAndroidInjector(modules = [PermissionRequestFragmentModule::class])
        abstract fun contributePermissionRequestFragment(): PermissionRequestFragment
    }
}

@Module
abstract class PermissionRequestFragmentModule : FragmentModule() {

    @Binds
    abstract fun bindPermissionRequestFragment(permissionRequestFragment: PermissionRequestFragment): Fragment

    companion object {

        @Provides
        @PermissionArray
        fun providePermissionArray(@FragmentBundle bundle: Bundle): Array<out String> =
            bundle.getStringArray(PermissionRequestFragment.ARGUMENT_KEY_PERMISSIONS) ?: emptyArray()
    }
}