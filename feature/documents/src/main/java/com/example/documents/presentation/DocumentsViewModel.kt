package com.example.documents.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Document
import com.example.domain.repository.DocumentsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DocumentsUiState(
    val isLoading: Boolean = true,
    val documents: List<Document> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class DocumentsViewModel @Inject constructor(
    private val repository: DocumentsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DocumentsUiState())
    val uiState: StateFlow<DocumentsUiState> =
        _uiState.asStateFlow()

    init {
        loadDocuments()
    }

    fun loadDocuments() {
        viewModelScope.launch {
            _uiState.value = DocumentsUiState(isLoading = true)

            repository.getDocuments()
                .onSuccess { documents ->
                    _uiState.value = DocumentsUiState(
                        isLoading = false,
                        documents = documents
                    )
                }
                .onFailure { exception ->
                    _uiState.value = DocumentsUiState(
                        isLoading = false,
                        error = exception.message
                            ?: "Не удалось загрузить документы"
                    )
                }
        }
    }
}