package com.example.habitflow_app.domain.usecases

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
     * Executes the user registration flow.
     * @param user User data to register
     * @return Registered user entity
     */
    suspend operator fun invoke(user: User): User {
        return authRepository.registerUser(user)
    }
}
