plugins {
    id("android-library-convention")
}

android {
    namespace = "com.pluu.webtoon.ui_common"

    buildFeatures.compose = true

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
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
