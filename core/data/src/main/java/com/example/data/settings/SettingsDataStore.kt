package com.example.data.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.domain.model.ServerSettings

private val Context.settingsDataStore by preferencesDataStore(
    name = "server_settings"
)

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val SERVER = stringPreferencesKey("server")
        val DATABASE = stringPreferencesKey("database")
        val USERNAME = stringPreferencesKey("username")
        val PASSWORD = stringPreferencesKey("password")
    }

    val settings: Flow<ServerSettings> =
        context.settingsDataStore.data.map { preferences ->
            ServerSettings(
                server = preferences[Keys.SERVER].orEmpty(),
                database = preferences[Keys.DATABASE].orEmpty(),
                username = preferences[Keys.USERNAME].orEmpty(),
                password = preferences[Keys.PASSWORD].orEmpty()
            )
        }

    suspend fun save(settings: ServerSettings) {
        context.settingsDataStore.edit { preferences ->
            preferences[Keys.SERVER] = settings.server
            preferences[Keys.DATABASE] = settings.database
            preferences[Keys.USERNAME] = settings.username
            preferences[Keys.PASSWORD] = settings.password
        }
    }
}