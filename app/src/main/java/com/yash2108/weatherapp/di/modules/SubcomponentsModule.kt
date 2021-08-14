package com.yash2108.weatherapp.di.modules

import com.yash2108.weatherapp.di.components.HomeActivityComponent
import dagger.Module

@Module(subcomponents = arrayOf(HomeActivityComponent::class))
class SubcomponentsModule {
}