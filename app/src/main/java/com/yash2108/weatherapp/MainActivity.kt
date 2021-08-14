package com.yash2108.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.yash2108.weatherapp.application.MyApplication
import com.yash2108.weatherapp.di.components.HomeActivityComponent

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private val viewModel by viewModels<HomeViewModel>()
    lateinit var activityComponent: HomeActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = (application as MyApplication).appComponent.homeActivityComponent().create()
        activityComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityComponent.inject(viewModel)
        initObservers()
        fetchData()
    }

    private fun initObservers() {
        viewModel.getDatabaseSource()?.observe(this, Observer {
            Log.d(TAG, "Found data: ${it.size}")
            it.forEach {
                Log.d(TAG, it.toString())
            }
        })
    }

    private fun fetchData() {
        viewModel.fetchWeatherInfo("noida")
    }
}