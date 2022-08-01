plugins {
    id("pluu.android.library")
    id("pluu.android.library.compose")
}

android {
    namespace = "com.pluu.webtoon.ui.compose.theme"
}

dependencies {
    // Compose
    implementation(libs.bundles.androidX.compose)
    debugImplementation(libs.bundles.androidX.compose.preview)
}