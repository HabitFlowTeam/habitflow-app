package com.example.habitflow_app.features.articles.di

import com.example.habitflow_app.domain.repositories.ArticleRepository
import com.example.habitflow_app.domain.usecases.GetAllArticlesUseCase
import com.example.habitflow_app.domain.usecases.GetRankedArticles
import com.example.habitflow_app.domain.usecases.GetUserTopLikedArticlesUseCase
import com.example.habitflow_app.domain.usecases.LikeArticleUseCase
import com.example.habitflow_app.domain.usecases.UnlikeArticleUseCase
import com.example.habitflow_app.domain.usecases.IsArticleLikedUseCase
import com.example.habitflow_app.features.articles.data.datasources.ArticleDataSource
import com.example.habitflow_app.features.articles.data.repositories.ArticleRepositoryImpl
import com.example.habitflow_app.features.articles.ui.viewmodel.ArticleViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ArticleRepositoryModule {
    @Provides
    @Singleton
    fun provideArticleRepository(
        articleDataSource: ArticleDataSource
    ): ArticleRepository {
        return ArticleRepositoryImpl(articleDataSource)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object ArticleViewModelModule {
    @Provides
    fun provideArticleViewModel(
        getUserTopLikedArticlesUseCase: GetUserTopLikedArticlesUseCase,
        getRankedArticles: GetRankedArticles,
        getAllArticlesUseCase: GetAllArticlesUseCase,
        likeArticleUseCase: LikeArticleUseCase,
        unlikeArticleUseCase: UnlikeArticleUseCase,
        isArticleLikedUseCase: IsArticleLikedUseCase
    ): ArticleViewModel {
        return ArticleViewModel(
            getUserTopLikedArticlesUseCase,
            getRankedArticles,
            getAllArticlesUseCase,
            likeArticleUseCase,
            unlikeArticleUseCase,
            isArticleLikedUseCase
        )
    }
}