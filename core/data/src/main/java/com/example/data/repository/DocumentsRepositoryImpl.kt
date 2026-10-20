package com.example.data.repository

import com.example.domain.model.Document
import com.example.domain.repository.DocumentsRepository
import com.example.data.remote.LocalDocumentsDataSource
import javax.inject.Inject

class DocumentsRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDocumentsDataSource
) : DocumentsRepository {

    override suspend fun getDocuments(): Result<List<Document>> {
        return runCatching {
            localDataSource.loadDocuments()
        }
    }
}