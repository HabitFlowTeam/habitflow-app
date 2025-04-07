// Import needed to read property files
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)    // Configures the module as an Android application
    alias(libs.plugins.kotlin.android)         // Adds Kotlin support for Android
    alias(libs.plugins.kotlin.compose)         // Enables Jetpack Compose features
    alias(libs.plugins.hilt.android)           // Adds Dagger Hilt for dependency injection
    alias(libs.plugins.kotlin.serialization)   // Enables Kotlin serialization for JSON parsing
    alias(libs.plugins.ksp)                    // Provides annotation processing for Kotlin
}

android {
    namespace = "com.example.habitflow_app"    // Unique package identifier for the app
    compileSdk = 35                            // Android SDK version used for compilation

    defaultConfig {
        applicationId =
            "com.example.habitflow_app"   // Unique application identifier on Google Play
        minSdk =
            28                                   // Minimum Android version supported (Android 9.0 Pie)
        targetSdk = 35                                // Target Android version for optimal behavior
        versionCode = 1                               // Internal version number for updates
        versionName = "1.0"                           // User-visible version name

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"  // Test runner for instrumented tests

        val localPropertiesFile =
            rootProject.file("local.properties")  // Configuration to read credentials from local.properties
        val localProperties = Properties().apply {
            localPropertiesFile.takeIf { it.exists() }?.reader()?.use { load(it) }
        } // Configuration to read credentials from local.properties

        buildConfigField(
            "String",
            "SUPABASE_URL",
            "\"${localProperties.getProperty("supabase.url") ?: ""}\""
        )  // Get supabase url
        buildConfigField(
            "String",
            "SUPABASE_KEY",
            "\"${localProperties.getProperty("supabase.key") ?: ""}\""
        )  // Get supabase key
    }

    buildTypes {
        release {
            isMinifyEnabled =
                false                   // Code shrinking disabled (enable for production)
            proguardFiles(                            // ProGuard rules for code optimization
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11  // Java source compatibility level
        targetCompatibility = JavaVersion.VERSION_11  // Java bytecode compatibility level
    }
    kotlinOptions {
        jvmTarget = "11"                              // Kotlin JVM target version
    }
    buildFeatures {
        buildConfig = true                            // Enable BuildConfig
        compose = true                                // Enables Jetpack Compose for UI
    }
}

dependencies {
    // Android Core Libraries
    implementation(libs.androidx.core.ktx)              // Kotlin extensions for Android core
    implementation(libs.androidx.lifecycle.runtime.ktx) // Lifecycle-aware components with Kotlin extensions

    // Jetpack Compose UI Framework
    implementation(libs.androidx.activity.compose)      // Integration between Compose and Activity
    implementation(platform(libs.androidx.compose.bom)) // Bill of Materials for Compose versions
    implementation(libs.androidx.ui)                    // Core Compose UI components
    implementation(libs.androidx.ui.graphics)           // Graphics rendering for Compose
    implementation(libs.androidx.ui.tooling.preview)    // Preview support for Compose
    implementation(libs.androidx.material3)             // Material Design 3 components for Compose
    implementation(libs.androidx.navigation.compose)    // Navigation framework for Compose
    debugImplementation(libs.androidx.ui.tooling)       // UI tooling for development builds

    // Dependency Injection
    implementation(libs.hilt.android)                   // Dagger Hilt runtime
    implementation(libs.hilt.navigation.compose)        // Integration between Hilt and Compose Navigation
    ksp(libs.hilt.compiler)                             // Hilt annotation processor

    // Supabase Backend Integration
    implementation(libs.supabase.auth)                  // Supabase main client and authentication
    implementation(libs.supabase.gotrue)                // Supabase authentication provider
    implementation(libs.supabase.postgrest)             // Supabase database access
    implementation(libs.supabase.realtime)              // Supabase real-time updates
    implementation(libs.supabase.storage)               // Supabase storage for files and media
    implementation(libs.ktor.client.core)               // Core Ktor client for making HTTP requests
    implementation(libs.ktor.client.android)            // Platform-specific engine to run Ktor on Android
    implementation(libs.ktor.client.content.negotiation) // Content negotiation feature (e.g., JSON serialization, content types)
    implementation(libs.ktor.serialization.kotlinx.json) // Kotlinx JSON serialization support for Ktor client
    implementation(libs.kotlinx.serialization.json)     // JSON serialization/deserialization

    // Networking
    implementation(libs.retrofit)                       // HTTP client for API calls
    implementation(libs.retrofit.converter.gson)        // Gson converter for Retrofit
    implementation(libs.okhttp.logging)                 // Logging interceptor for debugging API calls

    // Asynchronous Programming
    implementation(libs.kotlinx.coroutines.core)        // Kotlin coroutines core library
    implementation(libs.kotlinx.coroutines.android)     // Android-specific coroutines extensions

    // Image Loading and Caching
    implementation(libs.coil.compose)                   // Image loading library for Compose

    // Testing Libraries
    testImplementation(libs.junit)                      // Unit testing framework
    androidTestImplementation(libs.androidx.junit)      // Android-specific JUnit extensions
    androidTestImplementation(libs.androidx.espresso.core) // UI testing framework
    androidTestImplementation(platform(libs.androidx.compose.bom)) // BOM for Compose testing
    androidTestImplementation(libs.androidx.ui.test.junit4) // Compose UI testing with JUnit4
    debugImplementation(libs.androidx.ui.test.manifest) // Test manifest for Compose UI tests
}