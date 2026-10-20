package com.example.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url

interface PingApi {

    @GET
    suspend fun ping(
        @Url url: String,
        @Header("Authorization") authorization: String
    ): Response<ResponseBody>
}