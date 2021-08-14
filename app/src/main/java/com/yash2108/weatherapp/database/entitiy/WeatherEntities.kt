package com.yash2108.weatherapp.database.entitiy

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.yash2108.weatherapp.database.Constants

/**
 *
 * Demonstration 1-1 relationship
 */

@Entity(tableName = Constants.WeatherRequestTable)
data class Request(var type: String?,
                   @PrimaryKey
                   var query: String,
                   var language: String?,
                   var unit: String?)

@Entity(tableName = Constants.WeatherLocationTable, foreignKeys = arrayOf(
    ForeignKey(entity = Request::class,
        parentColumns = arrayOf("query"),
        childColumns = arrayOf("query"),
        onDelete = ForeignKey.CASCADE)
))
data class Location(
    @PrimaryKey
    var query: String,
    var name: String?,
    var country: String?)

@Entity(tableName = Constants.WeatherCurrentTable, foreignKeys = arrayOf(
    ForeignKey(entity = Location::class,
        parentColumns = arrayOf("query"),
        childColumns = arrayOf("query"),
        onDelete = ForeignKey.CASCADE)
))
data class Current(
    @PrimaryKey
    var query: String,
    var weather_code: String?,
    var observation_time: String?,
    var temperature: String?,
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
    var is_day: String?
)