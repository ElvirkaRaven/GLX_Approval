package com.example.documents.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.model.Document

@Composable
fun DocumentsScreen(
    uiState: DocumentsUiState,
    onRefresh: () -> Unit,
    onDocumentClick: (Document) -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Документы на согласование",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                TextButton(onClick = onRefresh) {
                    Text("Обновить")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            when {
                uiState.isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = "Загрузка документов…",
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }

                uiState.error != null -> {
                    Text(
                        text = "Не удалось загрузить документы",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )

                    Text(
                        text = uiState.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Button(
                        onClick = onRefresh,
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        Text("Повторить")
                    }
                }

                uiState.documents.isEmpty() -> {
                    Text(
                        text = "Документов пока нет",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )

                    Button(onClick = onRefresh) {
                        Text("Загрузить снова")
                    }
                }

                else -> {
                    Text(
                        text = "Всего документов: ${uiState.documents.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(
                            top = 4.dp,
                            bottom = 12.dp
                        )
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            items = uiState.documents,
                            key = { document -> document.id }
                        ) { document ->
                            DocumentCard(
                                document = document,
                                onClick = {
                                    onDocumentClick(document)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DocumentCard(
    document: Document,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = document.type.ifBlank { "Документ" },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.padding(top = 6.dp))

            DocumentField(
                label = "Номер",
                value = document.number
            )

            DocumentField(
                label = "Дата",
                value = document.date
            )

            DocumentField(
                label = "Сумма",
                value = buildString {
                    append(document.amount)
                    if (document.currency.isNotBlank()) {
                        append(" ")
                        append(document.currency)
                    }
                }
            )

            if (document.status.isNotBlank()) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                DocumentField(
                    label = "Статус",
                    value = document.status
                )
            }
        }
    }
}

@Composable
private fun DocumentField(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(0.35f)
        )

        Text(
            text = value.ifBlank { "—" },
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.65f)
        )
    }
}