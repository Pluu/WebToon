plugins {
    id("pluu.android.library")
    id("pluu.android.library.compose")
    kotlin("plugin.serialization")
}

android {
    namespace = "com.pluu.webtoon.ui_common"
}

dependencies {
    api(projects.model)
    api(projects.uiTheme)

    implementation(libs.kotlin.serialization)

    // Compose
    implementation(libs.androidX.activity.compose)

    // Image Loader
    implementation(libs.landscapist)
}
