package com.yash2108.weatherapp.di.modules

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.yash2108.openissuesreader.network.service.RetrofitAPI
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun providesRetrofitApi(retrofit: Retrofit) = retrofit.create(RetrofitAPI::class.java)

    @Provides
    @Singleton
    fun providesRetrofitClient(okhttpClient: OkHttpClient,
                               converterFactory: GsonConverterFactory) =
        Retrofit.Builder()
            .client(okhttpClient)
            .baseUrl("https://api.weatherstack.com/")
            .addConverterFactory(converterFactory)
            .build()

    @Provides
    @Singleton
    fun providesGsonConverterFactory() =
        GsonConverterFactory.create()

    @Provides
    @Singleton
    fun providesGsonInstance(): Gson = Gson()

    @Provides
    fun providesOkhttpClient(cache: Cache, httpLoggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient()
            .newBuilder()
            .cache(cache)
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun providesCache(cacheFile: File) = Cache(cacheFile, 10 * 1000 * 1000L)

    @Provides
    @Singleton
    fun providesFile(context: Application): File {
        val file = File(context.cacheDir, "HttpCache")
        file.mkdirs()
        return file
    }

    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Log.d("Okhttp: ", it)
        })

        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return interceptor
    }

}