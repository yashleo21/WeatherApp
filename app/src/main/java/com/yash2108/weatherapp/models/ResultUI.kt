package com.yash2108.weatherapp.models

sealed class ResultUI<out T>() {

    class Loading: ResultUI<Nothing>()

    class Success<T>(val data: T): ResultUI<T>()

    class Error(val error: Throwable): ResultUI<Nothing>()

    companion object {
        fun loading(): ResultUI<Nothing> = Loading()
        fun <T> success(data: T): ResultUI<T> = Success(data)
        fun error(error: Throwable): ResultUI<Nothing> = Error(error)
    }
}
