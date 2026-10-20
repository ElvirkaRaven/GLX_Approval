package com.example.data.di

import com.example.data.remote.PingApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import com.example.data.remote.UnsafeOkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return UnsafeOkHttpClient.create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://localhost/")
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun providePingApi(
        retrofit: Retrofit
    ): PingApi {
        return retrofit.create(PingApi::class.java)
    }
}