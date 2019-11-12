package io.github.jsamol.mvitemplate.app.di.module

import android.os.Bundle
import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.github.jsamol.mvitemplate.app.di.qualifier.FragmentBundle
import io.github.jsamol.mvitemplate.app.di.qualifier.permission.PermissionArray
import io.github.jsamol.mvitemplate.util.permission.PermissionRequestFragment

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

@Module
abstract class PermissionRequestFragmentModule : FragmentModule() {

    @Binds
    abstract fun bindPermissionRequestFragment(permissionRequestFragment: PermissionRequestFragment): Fragment

    @Module
    companion object {

        @JvmStatic
        @Provides
        @PermissionArray
        fun providePermissionArray(@FragmentBundle bundle: Bundle): Array<out String> =
            bundle.getStringArray(PermissionRequestFragment.ARGUMENT_KEY_PERMISSIONS) ?: emptyArray()
    }
}