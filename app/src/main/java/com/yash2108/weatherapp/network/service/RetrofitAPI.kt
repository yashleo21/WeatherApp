package com.yash2108.openissuesreader.network.service

import com.yash2108.openissuesreader.database.entity.HomeDataObject
import com.yash2108.openissuesreader.models.DetailDataObject
import com.yash2108.openissuesreader.network.Endpoints
import retrofit2.http.GET
import retrofit2.http.Url

interface RetrofitAPI {

    @GET(Endpoints.OKHTTP_ISSUES)
    suspend fun getIssuesList(): List<HomeDataObject>

    @GET
    suspend fun getComments(@Url url: String): List<DetailDataObject>
}