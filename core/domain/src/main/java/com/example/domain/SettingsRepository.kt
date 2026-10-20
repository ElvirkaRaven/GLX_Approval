package com.example.domain

import kotlinx.coroutines.flow.Flow
import com.example.domain.model.ServerSettings

interface SettingsRepository {
    fun observeSettings(): Flow<ServerSettings>

    suspend fun saveSettings(settings: ServerSettings)
}