plugins {
    id("pluu.android.library")
    id("pluu.android.library.compose")
    id("pluu.android.hilt")
}

android {
    namespace = "com.pluu.webtoon.weekly"
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
    implementation(libs.androidX.activity.compose)
    implementation(libs.androidX.constraintlayout.compose)
    implementation(libs.androidX.lifecycle.viewModel.compose)

    // Image Loader
    implementation(libs.landscapist)

    implementation(libs.timber)
}
