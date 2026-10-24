package com.example.documents

import com.example.domain.model.Document
import com.example.domain.repository.DocumentsRepository
import com.example.documents.presentation.DocumentsViewModel
import com.example.documents.presentation.DocumentDetailViewModel
import androidx.lifecycle.SavedStateHandle
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
class DocumentViewModelTest {

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
    fun `loads document with requested id`() = runTest(dispatcher) {
        val expectedDocument = Document(
            id = "doc-2",
            type = "бит_ПроектДоговора",
            number = "КАБП-004674",
            date = "28.09.2026",
            amount = "42119591,14",
            currency = "руб.",
            status = "На согласовании",
            project = "Тестовый проект",
            comment = "Проверка документа"
        )

        repository.result = Result.success(
            listOf(expectedDocument)
        )

        val handle = SavedStateHandle(
            mapOf("documentId" to "doc-2")
        )

        val viewModel = DocumentDetailViewModel(
            savedStateHandle = handle,
            repository = repository
        )

        advanceUntilIdle()

        assertEquals(
            expectedDocument,
            viewModel.uiState.value.document
        )
        assertNull(viewModel.uiState.value.error)
    }

}
