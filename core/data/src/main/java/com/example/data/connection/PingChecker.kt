package com.example.data.connection

import com.example.data.remote.PingApi
import com.example.domain.model.ServerSettings
import okhttp3.Credentials
import okhttp3.Credentials.basic
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PingChecker @Inject constructor(
    retrofit: Retrofit
) {
    private val pingApi = retrofit.create(PingApi::class.java)

    suspend fun check(settings: ServerSettings) {
        val server = settings.server.trim().trimEnd('/')
        val database = settings.database.trim().trim('/')

        require(server.isNotBlank()) {
            "Укажи адрес сервера"
        }
        require(database.isNotBlank()) {
            "Укажи название базы данных"
        }
        require(settings.username.isNotBlank()) {
            "Укажи имя пользователя"
        }
        require(settings.password.isNotBlank()) {
            "Укажи пароль"
        }

        val serverUrl = if (
            server.startsWith("https://", ignoreCase = true) ||
            server.startsWith("http://", ignoreCase = true)
        ) {
            server
        } else {
            "https://$server"
        }

        val url = "$serverUrl/$database/hs/approval/v1/ping/"

        val credentials = "${settings.username.trim()}:${settings.password}"
        val encoded = android.util.Base64.encodeToString(
            credentials.toByteArray(Charsets.UTF_8),
            android.util.Base64.NO_WRAP
        )
        val authorization = "Basic $encoded"

        android.util.Log.d("PingChecker", "URL: $url")
        android.util.Log.d("PingChecker", "Username: '${settings.username.trim()}'")

        val response = pingApi.ping(url, authorization)

        android.util.Log.d("PingChecker", "HTTP: ${response.code()}")
        android.util.Log.d(
            "PingChecker",
            "WWW-Authenticate: ${response.headers()["WWW-Authenticate"]}"
        )

        if (!response.isSuccessful) {
            val authHeader = response.headers()["WWW-Authenticate"]
                ?: "не указан"

            throw IOException(
                "HTTP ${response.code()}; WWW-Authenticate: $authHeader"
            )
        }

        val body = response.body()?.string()?.trim()

        if (body != "pong") {
            throw IOException(
                "Неожиданный ответ сервера: ${body ?: "пустой ответ"}"
            )
        }
    }
}