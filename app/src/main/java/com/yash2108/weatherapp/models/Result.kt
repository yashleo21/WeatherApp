package com.yash2108.weatherapp.models

sealed class Result<out T>() {

    class Loading: Result<Nothing>()

    class Success<T>(val data: T): Result<T>()

    class Error(val error: Throwable): Result<Nothing>()

    companion object {
        fun loading(): Result<Nothing> = Loading()
        fun <T> success(data: T): Result<T> = Success(data)
        fun error(error: Throwable): Result<Nothing> = Error(error)
    }
}
