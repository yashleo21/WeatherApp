package com.yash2108.weatherapp.di.components

import com.yash2108.openissuesreader.ui.di.scopes.ActivityScoped
import com.yash2108.weatherapp.HomeViewModel
import com.yash2108.weatherapp.MainActivity
import dagger.Subcomponent

@ActivityScoped
@Subcomponent
interface HomeActivityComponent {


    @Subcomponent.Factory
    interface Factory {
        fun create(): HomeActivityComponent
    }

    fun inject(activity: MainActivity)
    fun inject(viewModel: HomeViewModel)
}