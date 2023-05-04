plugins {
    id("pluu.android.library")
    id("pluu.android.library.compose")
    id("pluu.android.hilt")
}

android {
    namespace = "com.pluu.webtoon.episode"

    buildFeatures.viewBinding = true
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreAndroid)
    implementation(projects.domain)
    implementation(projects.uiCommon)
    implementation(projects.compose)

    implementation(libs.androidX.core.ktx)
    implementation(libs.androidX.paging.runtime)
    implementation(libs.androidX.paging.compose)
    implementation(libs.androidX.hilt.navigation.compose)

    // Compose
    implementation(libs.androidX.constraintlayout.compose)

    implementation(libs.coil.compose)
    implementation(libs.accompanist.systemUi)

    implementation(libs.timber)
}
