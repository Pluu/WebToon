plugins {
    id("pluu.android.library")
    id("pluu.android.library.compose")
}

android {
    namespace = "com.pluu.compose"
}

dependencies {
    debugImplementation(projects.uiTheme)

    // AndroidX
    implementation(libs.androidX.core.ktx)
    implementation(libs.androidX.preference)

    // Compose
    implementation(libs.bundles.androidX.compose)
    debugImplementation(libs.bundles.androidX.compose.preview)
}