package com.yash2108.weatherapp.models

sealed class ResultUI<out T>() {

    class Loading: ResultUI<Nothing>()

    class Success<T>(data: T): ResultUI<T>()

    class Error(error: Throwable): ResultUI<Throwable>()

    companion object {
        fun loading(): ResultUI<Nothing> = Loading()
        fun <T> success(data: T): ResultUI<T> = Success(data)
        fun error(error: Throwable): ResultUI<Throwable> = Error(error)
    }
}
