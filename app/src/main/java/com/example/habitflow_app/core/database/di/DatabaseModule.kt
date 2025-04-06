package com.example.habitflow_app.core.database.di

import android.content.Context
import com.example.habitflow_app.core.database.SupabaseConfig
import com.example.habitflow_app.core.database.SupabaseManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt Module that provides database-related dependencies for the application.
 *
 * This module is installed in the SingletonComponent, meaning all provided dependencies
 * will have a single instance throughout the application lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides a singleton instance of SupabaseConfig.
     *
     * @return Configured SupabaseConfig instance containing Supabase connection parameters
     */
    @Provides
    @Singleton
    fun provideSupabaseConfig(): SupabaseConfig {
        return SupabaseConfig()
    }

    /**
     * Provides a singleton instance of SupabaseManager.
     *
     * @param context Application context required for Supabase initialization
     * @param supabaseConfig Pre-configured Supabase settings
     * @return Initialized SupabaseManager instance for database operations
     */
    @Provides
    @Singleton
    fun provideSupabaseManager(
        @ApplicationContext context: Context,
        supabaseConfig: SupabaseConfig
    ): SupabaseManager {
        return SupabaseManager(context, supabaseConfig)
    }
}
