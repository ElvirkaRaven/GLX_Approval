package com.example.documents

import com.example.domain.model.Document
import com.example.domain.repository.DocumentsRepository
import com.example.documents.presentation.DocumentsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DocumentsViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var repository: FakeDocumentsRepository
    private lateinit var viewModel: DocumentsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = FakeDocumentsRepository()
        viewModel = DocumentsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `successful loading displays documents`() = runTest(dispatcher) {
        val expected = listOf(
            Document(
                id = "1",
                type = "бит_ЗаявкаНаЗатраты",
                number = "ТУБП-000373",
                date = "28.09.2026",
                amount = "368778,12",
                currency = "руб.",
                status = "На согласовании",
                project = "Тестовый проект",
                comment = "Тестовый комментарий"
            )
        )

        repository.result = Result.success(expected)

        viewModel.loadDocuments()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(expected, viewModel.uiState.value.documents)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `failed loading displays error`() = runTest(dispatcher) {
        repository.result = Result.failure(
            IllegalStateException("Ошибка загрузки")
        )

        viewModel.loadDocuments()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(
            "Ошибка загрузки",
            viewModel.uiState.value.error
        )
        assertEquals(
            emptyList<Document>(),
            viewModel.uiState.value.documents
        )
    }
}

class FakeDocumentsRepository : DocumentsRepository {

    var result: Result<List<Document>> =
        Result.success(emptyList())

    override suspend fun getDocuments(): Result<List<Document>> {
        return result
    }
}