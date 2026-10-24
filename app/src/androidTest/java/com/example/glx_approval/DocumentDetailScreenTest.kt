package com.example.glx_approval

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import com.example.documents.presentation.DocumentDetailScreen
import com.example.documents.presentation.DocumentDetailUiState
import com.example.domain.model.Document

class DocumentDetailScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun documentDetailsAndApproveActionWork() {
        var approved = false

        val document = Document(
            id = "doc-1",
            type = "бит_ЗаявкаНаЗатраты",
            number = "ТУБП-000373",
            date = "28.09.2026",
            amount = "368778,12",
            currency = "руб.",
            status = "На согласовании",
            project = "Тестовый проект",
            comment = "Тестовый комментарий"
        )

        composeRule.setContent {
            DocumentDetailScreen(
                uiState = DocumentDetailUiState(
                    isLoading = false,
                    document = document
                ),
                onBack = {},
                onApprove = { approved = true },
                onReject = {}
            )
        }

        composeRule
            .onNodeWithText("ТУБП-000373")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Тестовый проект")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Тестовый комментарий")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Согласовать")
            .assertIsDisplayed()
            .performClick()

        composeRule.runOnIdle {
            assertTrue(approved)
        }
    }
}