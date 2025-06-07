package com.example.habitflow_app.features.gamification.di

import com.example.habitflow_app.core.utils.ExtractInfoToken
import com.example.habitflow_app.domain.repositories.AuthRepository
import com.example.habitflow_app.domain.repositories.GamificationRepository
import com.example.habitflow_app.domain.usecases.GetCategoriesUseCase
import com.example.habitflow_app.domain.usecases.GetGlobalRankingUseCase
import com.example.habitflow_app.features.authentication.data.datasources.LocalDataStore
import com.example.habitflow_app.features.gamification.data.datasources.GamificationDataSource
import com.example.habitflow_app.features.gamification.data.repositories.GamificationRepositoryImpl
import com.example.habitflow_app.features.gamification.ui.viewmodel.StatsGlobalViewModel
import com.example.habitflow_app.features.gamification.ui.viewmodel.StatsPersonalViewModel
import com.example.habitflow_app.domain.usecases.GetUserHabitCategoriesUseCase
import com.example.habitflow_app.domain.repositories.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides gamification-related dependencies.
 * Installed in the SingletonComponent to ensure single instance across the app.
 */
@Module
@InstallIn(SingletonComponent::class)
object GamificationModule {

    /**
     * Provides singleton instance of GamificationRepository.
     *
     * @param gamificationDataSource Injected data source instance
     * @return Configured GamificationRepository implementation
     */
    @Provides
    @Singleton
    fun provideGamificationRepository(
        gamificationDataSource: GamificationDataSource
    ): GamificationRepository {
        return GamificationRepositoryImpl(gamificationDataSource)
    }

    /**
     * Provides singleton instance of StatsGlobalViewModel.
     *
     * @param getGlobalRankingUseCase Injected use case for global ranking
     * @param getCategoriesUseCase Injected use case for categories
     * @param localDataStore Injected local data store instance
     * @param extractInfoToken Injected token info extractor
     * @return Configured StatsGlobalViewModel instance
     */
    @Provides
    @Singleton
    fun provideStatsGlobalViewModel(
        getGlobalRankingUseCase: GetGlobalRankingUseCase,
        getCategoriesUseCase: GetCategoriesUseCase,
        localDataStore: LocalDataStore,
        extractInfoToken: ExtractInfoToken
    ): StatsGlobalViewModel {
        return StatsGlobalViewModel(
            getGlobalRankingUseCase,
            getCategoriesUseCase,
            localDataStore,
            extractInfoToken
        )
    }

    /**
     * Provides singleton instance of StatsPersonalViewModel.
     *
     * @param getUserHabitCategoriesUseCase Injected use case for user habit categories
     * @param profileRepository Injected profile repository instance
     * @return Configured StatsPersonalViewModel instance
     */
    @Provides
    @Singleton
    fun provideStatsPersonalViewModel(
        getUserHabitCategoriesUseCase: GetUserHabitCategoriesUseCase,
        profileRepository: ProfileRepository,
        extractInfoToken: ExtractInfoToken,
        authRepository: AuthRepository,
    ): StatsPersonalViewModel {
        return StatsPersonalViewModel(getUserHabitCategoriesUseCase, profileRepository, extractInfoToken, authRepository)
    }
}
