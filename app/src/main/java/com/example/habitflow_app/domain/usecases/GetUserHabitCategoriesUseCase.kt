package com.example.habitflow_app.domain.usecases

import com.example.habitflow_app.domain.models.HabitWithCategory
import com.example.habitflow_app.domain.repositories.HabitsRepository
import javax.inject.Inject

class GetUserHabitCategoriesUseCase @Inject constructor(
    private val habitsRepository: HabitsRepository
) {
    suspend operator fun invoke(userId: String): List<HabitWithCategory> {
        return habitsRepository.getUserHabitCategoriesView(userId)
    }
}

