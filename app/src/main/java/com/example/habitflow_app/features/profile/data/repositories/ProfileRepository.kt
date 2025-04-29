package com.example.habitflow_app.features.profile.data.repositories

import com.example.habitflow_app.domain.models.Profile
import com.example.habitflow_app.features.profile.data.datasources.ProfileDataSource
import com.example.habitflow_app.domain.repositories.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource
) : ProfileRepository {
    override suspend fun getProfile(userId: String): Profile {
        return profileDataSource.getProfile(userId)
    }
}