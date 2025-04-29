package com.example.habitflow_app.domain.repositories

import com.example.habitflow_app.domain.models.Profile

interface ProfileRepository {
    suspend fun getProfile(userId: String): Profile
}