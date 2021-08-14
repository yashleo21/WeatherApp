package com.yash2108.weatherapp.models

import androidx.room.Embedded
import androidx.room.Relation
import com.google.gson.annotations.SerializedName
import com.yash2108.weatherapp.database.entitiy.Current
import com.yash2108.weatherapp.database.entitiy.Location
import com.yash2108.weatherapp.database.entitiy.Request

data class WeatherContainer(var request: WeatherRequest?,
                            var location: WeatherLocation?,
                            var current: WeatherCurrent?)

data class WeatherRequest(var type: String?, var query: String?, var language: String?, var unit: String?)

data class WeatherLocation(var name: String?, var country: String?, var region: String?,
                           var lat: String?, var lon: String?, var timezone_id: String?,
                           var localtime: String?, var localtime_epoch: String?,
                           var utc_offset: String?)


data class WeatherCurrent(var observation_time: String?,
                          var temperature: String?,
                          var weather_code: String?,
                          var weather_icons: ArrayList<String>?,
                          var weather_description: ArrayList<String>?,
                          var wind_speed: String?,
                          var wind_degree: String?,
                          var wind_dir: String?,
                          var pressure: String?,
                          var precip: String?,
                          var humidity: String?,
                          var cloudcover: String?,
                          var feelslike: String?,
                          var uv_index: String?,
                          var visibility: String?,
                          var is_day: String?)

data class WeatherResponse(
    @Embedded val request: Request,
    @Relation(entity = Location::class, parentColumn = "query", entityColumn = "query")
    val locationWithCurrentData: LocationWithCurrentData?
)

data class LocationWithCurrentData(
    @Embedded val location: Location,
    @Relation(parentColumn = "query", entityColumn = "query")
    val current: Current?
)