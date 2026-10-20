package com.example.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    SettingsScreen(
        settings = uiState.settings,
        isSaving = uiState.isSaving,
        isCheckingConnection = uiState.isCheckingConnection,
        message = uiState.message,
        isConnectionSuccessful = uiState.isConnectionSuccessful,
        onServerChange = viewModel::updateServer,
        onDatabaseChange = viewModel::updateDatabase,
        onUsernameChange = viewModel::updateUsername,
        onPasswordChange = viewModel::updatePassword,
        onSave = viewModel::saveSettings,
        onCheckConnection = viewModel::checkConnection
    )
}