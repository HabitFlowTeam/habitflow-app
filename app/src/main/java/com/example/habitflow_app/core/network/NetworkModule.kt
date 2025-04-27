package com.example.habitflow_app.core.network

import com.example.habitflow_app.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Dagger Hilt module that provides network-related dependencies for API communication.
 * Installed in the SingletonComponent to ensure single instance of network clients across the app.
 *
 * This module handles the configuration and creation of:
 * - OkHttpClient with logging and timeout settings
 * - Retrofit instance for Directus API service
 */
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    companion object {
        /**
         * Default timeout duration for network operations in seconds.
         * Applied to connect, read and write operations.
         */
        private const val TIMEOUT = 30L

        /**
         * Creates and configures an OkHttpClient instance with interceptors and timeout settings.
         *
         * @return Configured OkHttpClient instance with:
         * - Logging interceptor (only in debug mode)
         * - Connection, read and write timeouts
         */
        @Provides
        @Singleton
        private fun createOkHttpClient(): OkHttpClient {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                // Enable full HTTP body logging in debug builds, no logging in release
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }

            return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build()
        }

        /**
         * Creates and configures a Retrofit service for Directus API communication.
         *
         * @return Configured DirectusApiService instance with:
         * - Base URL from build config
         * - Custom OkHttpClient
         * - GSON converter factory for JSON serialization/deserialization
         */
        @Provides
        @Singleton
        fun createDirectusApiService(): DirectusApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.DIRECTUS_URL)
                .client(createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(DirectusApiService::class.java)
        }
    }
}
