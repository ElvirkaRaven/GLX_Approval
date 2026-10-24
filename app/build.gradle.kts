plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
    id("io.gitlab.arturbosch.detekt")
}

android {
    namespace = "ru.example.glx_approval"
    compileSdk = 37

    defaultConfig {
        applicationId = "ru.example.glx_approval"
        minSdk = 26
        targetSdk = 36

        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(platform(libs.compose.bom))
    androidTestImplementation(platform(libs.compose.bom))

    implementation(libs.activity.compose)
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.preview)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.navigation.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":feature:documents"))
    implementation(project(":feature:settings"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    testImplementation("androidx.datastore:datastore-preferences:1.1.7")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

detekt {
    toolVersion = "1.23.8"
    config.setFrom(
        files("$rootDir/config/detekt/detekt.yml")
    )
    buildUponDefaultConfig = true
}