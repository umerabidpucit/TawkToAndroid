import java.util.*

// Load credentials.properties file
val credentialsProperties = Properties()
val credentialsFile = rootProject.file("credentials.properties")

if (credentialsFile.exists()) {
    credentialsProperties.load(credentialsFile.inputStream())
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    id("kotlin-parcelize")
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.compose.material3)

    // Retrofit for HTTP requests
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Gson for JSON parsing
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Coroutines support for Retrofit
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("io.coil-kt:coil-compose:2.4.0")

    // JUnit for unit tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")

    // Mockito for mocking
    testImplementation("org.mockito:mockito-core:4.8.0")

    // Mockito Kotlin extensions (for mocking with Kotlin)
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")

    // Kotlinx Coroutines Test for runTest and other testing utilities
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")

    // Optional: If you need to use Kotlin reflection for testing purposes
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:1.8.0")

    testImplementation("io.mockk:mockk:1.13.5")

    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")


    // database
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")
//    kapt("androidx.room:room-compiler:2.5.2")


}