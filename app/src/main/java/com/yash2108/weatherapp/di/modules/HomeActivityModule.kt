package com.yash2108.weatherapp.di.modules

import android.content.Context
import com.google.android.gms.location.LocationServices
import com.yash2108.openissuesreader.ui.di.scopes.ActivityScoped
import dagger.Module
import dagger.Provides

@Module
class HomeActivityModule {

    @ActivityScoped
    @Provides
    fun providesFusedLocationClient(context: Context) =
        LocationServices.getFusedLocationProviderClient(context)


}