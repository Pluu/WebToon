plugins {
    id("android-library-convention")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.pluu.webtoon.weekly"

    buildFeatures.compose = true

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreAndroid)
    implementation(projects.domain)
    implementation(projects.uiCommon)
    implementation(projects.compose)

    implementation(libs.androidX.core.ktx)
    implementation(libs.androidX.lifecycle.viewModel)
    implementation(libs.androidX.hilt.navigation.compose)
    implementation(libs.androidX.palette)

    // Compose
    implementation(libs.bundles.androidX.compose)
    debugImplementation(libs.androidX.compose.tooling)
    implementation(libs.androidX.activity.compose)
    implementation(libs.androidX.constraintlayout.compose)
    implementation(libs.androidX.lifecycle.viewModel.compose)

    implementation(libs.coil.compose)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pagerIndicators)
    implementation(libs.accompanist.systemUi)

    // Hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    // Coil
    implementation(libs.coil.core)

    implementation(libs.timber)
}

kapt {
    correctErrorTypes = true
}
