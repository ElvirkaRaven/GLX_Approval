package com.example.data.remote

import okhttp3.OkHttpClient
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


object UnsafeOkHttpClient {

    fun create(): OkHttpClient {
        val trustAllCertificates = object : X509TrustManager {
            override fun checkClientTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) = Unit

            override fun checkServerTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) = Unit

            override fun getAcceptedIssuers(): Array<X509Certificate> =
                emptyArray()
        }

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(
            null,
            arrayOf<TrustManager>(trustAllCertificates),
            java.security.SecureRandom()
        )

        return OkHttpClient.Builder()
            .sslSocketFactory(
                sslContext.socketFactory,
                trustAllCertificates
            )
            .hostnameVerifier { _, _ -> true }
            .build()
    }
}