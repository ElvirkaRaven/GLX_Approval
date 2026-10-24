package com.example.data.local

import com.example.domain.model.Document

interface DocumentsDataSource {
    fun loadDocuments(): List<Document>
}