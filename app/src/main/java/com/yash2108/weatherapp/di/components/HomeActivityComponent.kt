package com.yash2108.weatherapp.di.components

import android.content.Context
import com.yash2108.openissuesreader.ui.di.scopes.ActivityScoped
import com.yash2108.weatherapp.HomeViewModel
import com.yash2108.weatherapp.MainActivity
import com.yash2108.weatherapp.di.modules.HomeActivityModule
import dagger.Binds
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScoped
@Subcomponent(modules = arrayOf(HomeActivityModule::class))
interface HomeActivityComponent {


    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: Context): HomeActivityComponent
    }

    fun inject(activity: MainActivity)
    fun inject(viewModel: HomeViewModel)
}