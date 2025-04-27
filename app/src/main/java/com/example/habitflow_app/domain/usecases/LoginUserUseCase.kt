package com.example.habitflow_app.domain.usecases

import com.example.habitflow_app.domain.models.User
import com.example.habitflow_app.domain.repositories.AuthRepository
import javax.inject.Inject

/**
 * Encapsulates the business logic for user login.
 *
 * @property authRepository The authentication data source
 */
class LoginUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Executes the user login flow.
     * @param email User email address
     * @param password User password
     * @return Logged-in user entity
     */
    suspend operator fun invoke(email: String, password: String): User {
        return authRepository.login(email, password)
    }
}
