plugins {
    id("pluu.android.library")
    id("pluu.android.library.compose")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.pluu.webtoon.intro"
}

dependencies {
    implementation(projects.uiCommon)
    implementation(projects.compose)

    implementation(libs.androidX.core.ktx)
    implementation(libs.androidX.splashScreen)

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
