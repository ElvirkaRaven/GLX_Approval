package com.example.domain.repository

import com.example.domain.model.Document

interface DocumentsRepository {
    suspend fun getDocuments(): Result<List<Document>>
}
