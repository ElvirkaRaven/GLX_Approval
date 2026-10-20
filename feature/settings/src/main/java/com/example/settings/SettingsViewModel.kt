package com.example.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.connection.PingChecker
import com.example.data.settings.SettingsDataStore
import com.example.domain.model.ServerSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val settings: ServerSettings = ServerSettings(),
    val isSaving: Boolean = false,
    val isCheckingConnection: Boolean = false,
    val message: String? = null,
    val isConnectionSuccessful: Boolean? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val pingChecker: PingChecker
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsDataStore.settings.collect { settings ->
                _uiState.update {
                    it.copy(settings = settings)
                }
            }
        }
    }

    fun updateServer(value: String) {
        updateSettings { copy(server = value) }
    }

    fun updateDatabase(value: String) {
        updateSettings { copy(database = value) }
    }

    fun updateUsername(value: String) {
        updateSettings { copy(username = value) }
    }

    fun updatePassword(value: String) {
        updateSettings { copy(password = value) }
    }

    private fun updateSettings(
        change: ServerSettings.() -> ServerSettings
    ) {
        _uiState.update {
            it.copy(
                settings = it.settings.change(),
                message = null,
                isConnectionSuccessful = null
            )
        }
    }

    fun saveSettings() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isSaving = true, message = null)
            }

            try {
                settingsDataStore.save(_uiState.value.settings)

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        message = "Настройки сохранены"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        message = "Ошибка сохранения: ${e.message}"
                    )
                }
            }
        }
    }

    fun checkConnection() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isCheckingConnection = true,
                    message = null,
                    isConnectionSuccessful = null
                )
            }

            try {
                pingChecker.check(_uiState.value.settings)

                _uiState.update {
                    it.copy(
                        isCheckingConnection = false,
                        isConnectionSuccessful = true,
                        message = "Подключение успешно. Сервер ответил pong."
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isCheckingConnection = false,
                        isConnectionSuccessful = false,
                        message = "Ошибка подключения: ${
                            e.message ?: "не удалось связаться с сервером"
                        }"
                    )
                }
            }
        }
    }
}