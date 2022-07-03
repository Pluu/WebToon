plugins {
    id("android-library-convention")
}

android {
    namespace = "com.pluu.compose"

    buildFeatures.compose = true

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
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