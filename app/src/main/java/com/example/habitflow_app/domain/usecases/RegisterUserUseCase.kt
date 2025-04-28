package com.example.habitflow_app.domain.usecases

import com.example.habitflow_app.domain.models.Profile
import com.example.habitflow_app.domain.models.User
import com.example.habitflow_app.domain.repositories.AuthRepository
import javax.inject.Inject

/**
 * Encapsulates the business logic for user registration.
 *
 * @property authRepository The authentication data source
 */
class RegisterUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Executes the complete user registration flow:
     * 1. Register user in Directus
     * 2. Create user profile
     *
     * @param user User credentials and basic info
     * @param profile User profile details
     * @return Registered user entity with ID
     */
    suspend operator fun invoke(user: User, profile: Profile): User {
        return authRepository.registerUser(user, profile)
    }
}
