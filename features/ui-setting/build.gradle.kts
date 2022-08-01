plugins {
    id("pluu.android.library")
    id("pluu.android.library.compose")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.pluu.webtoon.setting"
}

dependencies {
    implementation(projects.uiCommon)
    implementation(projects.model)
    implementation(projects.domain)
    implementation(projects.compose)

    // AndroidX
    implementation(libs.androidX.core.ktx)
    implementation(libs.androidX.hilt.navigation.compose)
    implementation(libs.androidX.preference)

    // Compose
    implementation(libs.bundles.androidX.compose)
    debugImplementation(libs.bundles.androidX.compose.preview)
    implementation(libs.androidX.activity.compose)

    implementation(libs.accompanist.systemUi)

    // Hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
}

kapt {
    correctErrorTypes = true
}
