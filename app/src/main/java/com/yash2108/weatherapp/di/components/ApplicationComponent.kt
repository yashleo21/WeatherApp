package com.yash2108.weatherapp.di.components

import android.app.Application
import com.yash2108.weatherapp.di.modules.DatabaseModule
import com.yash2108.weatherapp.di.modules.NetworkModule
import com.yash2108.weatherapp.di.modules.SubcomponentsModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        NetworkModule::class,
        DatabaseModule::class,
        SubcomponentsModule::class
    )
)
interface ApplicationComponent {


    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Application): ApplicationComponent
    }

    fun homeActivityComponent(): HomeActivityComponent.Factory
}