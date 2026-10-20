package com.example.domain.model

data class ObjectsRequest(
    val User: String,
    val Objects: String,
    val DateStart: String? = null,
    val DateFinish: String? = null
)