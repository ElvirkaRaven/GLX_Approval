package com.example.data


import com.example.data.local.DocumentsDataSource
import com.example.data.repository.DocumentsRepositoryImpl
import com.example.domain.model.Document
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DocumentsRepositoryImplTest {

    @Test
    fun `getDocuments returns documents when source succeeds`() = runTest {

        val expected = listOf(
            Document(
                id = "doc-1",
                type = "Соглашение NDA",
                number = "001",
                date = "2026-10-20",
                amount = "1500",
                currency = "RUB",
                status = "На согласовании"
            )
        )

        val dataSource = FakeDocumentsDataSource(documents = expected)
        val repository = DocumentsRepositoryImpl(dataSource)

        val result = repository.getDocuments()

        assertTrue(result.isSuccess)
        assertEquals(expected, result.getOrThrow())
    }

    @Test
    fun `getDocuments returns failure when source throws`() = runTest {

        val dataSource = FakeDocumentsDataSource(
            exception = IllegalStateException("Ошибка чтения JSON")
        )

        val repository = DocumentsRepositoryImpl(dataSource)

        val result = repository.getDocuments()

        assertTrue(result.isFailure)
        assertEquals(
            "Ошибка чтения JSON",
            result.exceptionOrNull()?.message
        )
    }

    private class FakeDocumentsDataSource(
        private val documents: List<Document> = emptyList(),
        private val exception: Exception? = null
    ) : DocumentsDataSource {

        override fun loadDocuments(): List<Document> {
            exception?.let { throw it }
            return documents
        }
    }
}