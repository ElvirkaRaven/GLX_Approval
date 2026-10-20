package com.example.domain.model

data class Document(
    val id: String,
    val type: String,
    val number: String,
    val date: String,
    val amount: String,
    val currency: String,
    val status: String,
    val project: String = "",
    val comment: String = ""
)