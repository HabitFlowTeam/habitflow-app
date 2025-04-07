package com.example.habitflow_app.core.database

import android.content.Context
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Main class for managing the Supabase client.
 * Provides access to all Supabase modules (Auth, Database, Storage).
 *
 * This is the central point for all Supabase operations in the app.
 */
@Singleton
class SupabaseManager @Inject constructor(
    private val context: Context,
    private val supabaseConfig: SupabaseConfig
) {
    /**
     * Main Supabase client configured with all necessary modules.
     */
    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = supabaseConfig.supabaseUrl,
            supabaseKey = supabaseConfig.supabaseKey
        ) {
            // Authentication module configuration
            install(Auth) {
                autoSaveToStorage = true // Persist auth session
                autoLoadFromStorage = true // Restore session on app restart
            }

            // PostgreSQL database module
            install(Postgrest) // For CRUD operations

            // Storage module for file operations
            install(Storage) // For file uploads/downloads
        }
    }
}
