// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false     // Android Gradle Plugin for building Android applications
    alias(libs.plugins.kotlin.android) apply false          // Kotlin plugin for Android development
    alias(libs.plugins.kotlin.compose) apply false          // Enables Jetpack Compose compiler optimizations
    alias(libs.plugins.kotlin.serialization) apply false    // Adds support for Kotlin serialization framework
    alias(libs.plugins.hilt.android) apply false            // Dagger Hilt for dependency injection
    alias(libs.plugins.ksp) apply false                     // Kotlin Symbol Processing for annotation processing
}