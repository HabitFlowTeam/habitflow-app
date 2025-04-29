package com.example.habitflow_app.features.authentication.data.datasources

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object LocalDataSourceProvider {
    private var instance: LocalDataStore? = null
    fun init(dataStore: DataStore<Preferences>) {
        if (instance == null) {
            instance = LocalDataStore(dataStore)
        }
    }

    fun getInstance(): LocalDataStore {
        return instance ?: throw IllegalStateException("LocalDataStore not initialized")
    }
}

class LocalDataStore(val dataStore: DataStore<Preferences>) {
    // Save
    suspend fun save(key: String, value: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    // Load
    fun load(key: String): Flow<String> = dataStore.data.map { preferences ->
        preferences[stringPreferencesKey(key)] ?: ""
    }
}