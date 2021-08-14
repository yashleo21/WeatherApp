package com.yash2108.weatherapp.models

interface HomeDataSource {

    fun getData(): WeatherResponse
}