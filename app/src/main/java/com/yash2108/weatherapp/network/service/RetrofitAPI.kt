package com.yash2108.openissuesreader.network.service

import com.yash2108.openissuesreader.network.Endpoints
import com.yash2108.weatherapp.models.WeatherContainer
import com.yash2108.weatherapp.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAPI {

    @GET(Endpoints.CURRENT_WEATHER)
    suspend fun getCurrentWeather(@Query("access_key") key: String,
                                  @Query("query") query: String): WeatherContainer
}