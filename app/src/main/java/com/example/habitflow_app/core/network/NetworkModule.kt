package com.example.habitflow_app.core.network

import com.example.habitflow_app.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.example.habitflow_app.features.authentication.data.datasources.LocalDataStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

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
object NetworkModule {
    /**
     * Default timeout duration for network operations in seconds.
     * Applied to connect, read and write operations.
     */
    private const val TIMEOUT = 30L

    /**
     * Interceptor that adds the authentication token to the request headers
     */
    private class AuthInterceptor(private val localDataStore: LocalDataStore) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()

            // Get the token synchronously (needed for the interceptor)
            val token = runBlocking {
                localDataStore.getAccessTokenOnce()
            }

            return if (token != null) {
                // Add the authorization header if we have a token
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            } else {
                // Proceed without a token if it is not available
                chain.proceed(originalRequest)
            }
        }
    }

    /**
     * Creates and configures an OkHttpClient instance with interceptors and timeout settings.
     *
     * @return Configured OkHttpClient instance with:
     * - Logging interceptor (only in debug mode)
     * - Connection, read and write timeouts
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(localDataStore: LocalDataStore): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // Enable full HTTP body logging in debug builds, no logging in release
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(localDataStore)) // Interceptor de autenticación
            .addInterceptor(loggingInterceptor) // Interceptor de logging (debe ir después)
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
    fun provideDirectusApiService(okHttpClient: OkHttpClient): DirectusApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.DIRECTUS_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(DirectusApiService::class.java)
    }
}
