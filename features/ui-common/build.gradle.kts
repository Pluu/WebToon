plugins {
    id("pluu.android.library")
    id("pluu.android.library.compose")
}

android {
    namespace = "com.pluu.webtoon.ui_common"
}

dependencies {
    api(projects.model)
    api(projects.uiTheme)

    // Android UI
    implementation(libs.androidX.fragment)

    // Compose
    implementation(libs.bundles.androidX.compose)
    debugImplementation(libs.bundles.androidX.compose.preview)
    implementation(libs.androidX.activity.compose)

    // Coil
    implementation(libs.coil.core)
}
