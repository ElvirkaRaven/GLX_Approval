package com.example.data

import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url
import com.example.domain.model.ObjectsRequest

interface ApprovalApi {

    @POST
    suspend fun getObjectsForApproval(
        @Url url: String,
        @Body request: ObjectsRequest
    ): Response<JsonElement>
}