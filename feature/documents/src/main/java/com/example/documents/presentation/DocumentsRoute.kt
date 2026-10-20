package com.example.documents.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.model.ServerSettings

@Composable
fun DocumentsRoute(
    settings: ServerSettings,
    onDocumentClick: (String) -> Unit = {},
    viewModel: DocumentsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(settings) {
        viewModel.loadDocuments()
    }

    DocumentsScreen(
        uiState = uiState,
        onRefresh = {
            viewModel.loadDocuments()
        },
        onDocumentClick = { document ->
            onDocumentClick(document.id)
        }
    )
}