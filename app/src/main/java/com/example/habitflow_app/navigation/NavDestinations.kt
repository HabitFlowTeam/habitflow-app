package com.example.habitflow_app.navigation

/**
 * Application navigation destinations.
 */
object NavDestinations {
    const val REGISTER = "register"
    const val LOGIN = "login"
    const val HOME = "home"
    const val PROFILE = "profile"
    const val HABITS = "habits"
    const val ARTICLES = "articles"
    const val GAMIFICATION = "gamification"
    const val MAIN = "main"
    const val FORGOT_PASSWORD = "forgot_password"
    const val CREATE_HABIT = "create_habit"
    const val EDIT_HABIT = "edit_habit/{habitId}"
    const val ARTICLE_DETAIL = "article_detail/{articleId}"
    const val CREATE_ARTICLE = "create_article"

    // Helper function to create edit habit route
    fun editHabitRoute(habitId: String) = "edit_habit/$habitId"

    // Helper function to create article detail route
    fun articleDetailRoute(articleId: String) = "article_detail/$articleId"
}
