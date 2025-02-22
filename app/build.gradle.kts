import java.util.Properties

// Load credentials.properties file
val credentialsProperties = Properties()
val credentialsFile = rootProject.file("credentials.properties")

if (credentialsFile.exists()) {
    credentialsProperties.load(credentialsFile.inputStream())
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("com.google.devtools.ksp") version "1.9.10-1.0.13"
}

android {
    namespace = "com.umtech.tawkandroid"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.umtech.tawkandroid"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // Inject BASE_URL from credentials.properties
        buildConfigField("String", "BASE_URL", "${credentialsProperties["BASE_URL"]}")
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // JUnit for unit tests
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.engine)

    // Mockito for mocking
    testImplementation(libs.mockito.core)

    // Mockito Kotlin extensions (for mocking with Kotlin)
    testImplementation(libs.mockito.kotlin)

    // Kotlinx Coroutines Test for runTest and other testing utilities
    testImplementation(libs.kotlinx.coroutines.test)

    // Optional: If you need to use Kotlin reflection for testing purposes

    testImplementation(libs.kotlin.reflect)

    testImplementation(libs.mockk)

    //androidx core
    implementation(libs.bundles.androidx.core)
    // compose
    implementation(libs.bundles.compose)
    // Networking
    implementation(libs.bundles.networking)
    // Dependency Injection
    implementation(libs.bundles.di)
    // Room
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)
    // Paging
    implementation(libs.bundles.paging)
    // UI
    implementation(libs.bundles.ui)
    // Coroutine
    implementation(libs.bundles.coroutines)

}