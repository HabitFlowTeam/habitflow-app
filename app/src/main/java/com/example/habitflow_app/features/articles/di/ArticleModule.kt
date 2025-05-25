package com.example.habitflow_app.features.articles.di

import com.example.habitflow_app.domain.repositories.ArticleRepository
import com.example.habitflow_app.domain.usecases.GetRankedArticles
import com.example.habitflow_app.domain.usecases.GetUserTopLikedArticlesUseCase
import com.example.habitflow_app.features.articles.data.datasources.ArticleDataSource
import com.example.habitflow_app.features.articles.data.repositories.ArticleRepositoryImpl
import com.example.habitflow_app.features.articles.ui.viewmodel.ArticleViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ArticleModule {
    @Provides
    @Singleton
    fun provideArticleRepository(
        articleDataSource: ArticleDataSource
    ): ArticleRepository {
        return ArticleRepositoryImpl(articleDataSource)
    }

    @Provides
    @Singleton
    fun provideArticleViewModel(
        getUserTopLikedArticlesUseCase: GetUserTopLikedArticlesUseCase,
        getRankedArticles: GetRankedArticles
    ): ArticleViewModel {
        return ArticleViewModel(
            getUserTopLikedArticlesUseCase,
            getRankedArticles
        )
    }
}