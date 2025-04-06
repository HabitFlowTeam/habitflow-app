package com.example.habitflow_app.core.database

import com.example.habitflow_app.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Configuration for the Supabase client.
 * Contains necessary credentials to connect to Supabase.
 *
 * This class is responsible for providing Supabase credentials.
 */
@Singleton
class SupabaseConfig @Inject constructor() {
    /**
     * URL of the Supabase instance.
     */
    val supabaseUrl: String = BuildConfig.SUPABASE_URL

    /**
     * Supabase API key.
     */
    val supabaseKey: String = BuildConfig.SUPABASE_KEY
}
