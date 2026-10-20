package com.example.documents.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.model.Document

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentDetailScreen(
    uiState: DocumentDetailUiState,
    onBack: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Документ") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("‹")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onApprove,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Согласовать")
                }

                OutlinedButton(
                    onClick = onReject,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Отклонить")
                }
            }
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            uiState.error != null -> {
                Text(
                    text = uiState.error,
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }

            uiState.document != null -> {
                DocumentDetails(
                    document = uiState.document,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun DocumentDetails(
    document: Document,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        DetailField("Тип документа", document.type)
        DetailField("Номер", document.number)
        DetailField("Дата", document.date)
        DetailField(
            "Сумма",
            "${document.amount} ${document.currency}".trim()
        )
        DetailField("Статус", document.status)

        Spacer(modifier = Modifier.height(12.dp))

        DetailField("Проект", document.project.ifBlank { "Не указан" })
        DetailField("Комментарий", document.comment.ifBlank { "Нет комментария" })
    }
}

@Composable
private fun DetailField(
    title: String,
    value: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}