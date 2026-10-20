package com.example.documents.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Document
import com.example.domain.repository.DocumentsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DocumentDetailUiState(
    val isLoading: Boolean = true,
    val document: Document? = null,
    val error: String? = null
)

@HiltViewModel
class DocumentDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: DocumentsRepository
) : ViewModel() {

    private val documentId: String =
        checkNotNull(savedStateHandle["documentId"])

    private val _uiState = MutableStateFlow(
        DocumentDetailUiState()
    )
    val uiState: StateFlow<DocumentDetailUiState> =
        _uiState.asStateFlow()

    init {
        loadDocument()
    }

    private fun loadDocument() {
        viewModelScope.launch {
            _uiState.value = DocumentDetailUiState(
                isLoading = true
            )

            repository.getDocuments()
                .onSuccess { documents ->
                    val document = documents.find {
                        it.id == documentId
                    }

                    _uiState.value = if (document != null) {
                        DocumentDetailUiState(
                            isLoading = false,
                            document = document
                        )
                    } else {
                        DocumentDetailUiState(
                            isLoading = false,
                            error = "Документ не найден"
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.value = DocumentDetailUiState(
                        isLoading = false,
                        error = error.message
                            ?: "Не удалось загрузить документ"
                    )
                }
        }
    }
}