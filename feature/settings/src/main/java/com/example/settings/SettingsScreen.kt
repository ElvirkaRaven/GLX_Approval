package com.example.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.domain.model.ServerSettings

@Composable
fun SettingsScreen(
    settings: ServerSettings,
    isSaving: Boolean,
    isCheckingConnection: Boolean,
    message: String?,
    isConnectionSuccessful: Boolean?,
    onServerChange: (String) -> Unit,
    onDatabaseChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSave: () -> Unit,
    onCheckConnection: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Настройки сервера",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = settings.server,
            onValueChange = onServerChange,
            label = { Text("Адрес сервера") },
            placeholder = { Text("https://server.example") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = settings.database,
            onValueChange = onDatabaseChange,
            label = { Text("Имя базы") },
            placeholder = { Text("НазваниеБазы") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = settings.username,
            onValueChange = onUsernameChange,
            label = { Text("Имя пользователя") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = settings.password,
            onValueChange = onPasswordChange,
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = onSave,
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isSaving) {
                CircularProgressIndicator()
            } else {
                Text("Сохранить настройки")
            }
        }

        Button(
            onClick = onCheckConnection,
            enabled = !isCheckingConnection,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isCheckingConnection) {
                CircularProgressIndicator()
            } else {
                Text("Проверить подключение")
            }
        }

        if (message != null) {
            Text(
                text = message,
                color = when (isConnectionSuccessful) {
                    true -> MaterialTheme.colorScheme.primary
                    false -> MaterialTheme.colorScheme.error
                    null -> MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}