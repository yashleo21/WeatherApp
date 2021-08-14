package com.yash2108.weatherapp.models

sealed class Result<out T>() {

    class Success<T>(data: T): Result<T>()

    class Error(error: Throwable): Result<Throwable>()

    companion object {
        fun <T> success(data: T): Result<T> = Success(data)
        fun error(error: Throwable): Result<Throwable> = Error(error)
    }
}
