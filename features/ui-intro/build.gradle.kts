plugins {
    id("android-library-convention")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    buildFeatures.compose = true

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
}

dependencies {
    implementation(projects.uiCommon)
    implementation(projects.compose)

    implementation(libs.androidX.core.ktx)
    implementation(libs.androidX.splashScreen)

    // Compose
    implementation(libs.bundles.androidX.compose)
    debugImplementation(libs.androidX.compose.tooling)
    implementation(libs.androidX.activity.compose)

    implementation(libs.accompanist.systemUi)

    // Hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
}

kapt {
    correctErrorTypes = true
}
