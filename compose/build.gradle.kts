plugins {
    id("android-library-convention")
}

android {
    buildFeatures.compose = true

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
}

dependencies {
    debugImplementation(projects.uiTheme)

    // AndroidX
    implementation(libs.androidX.core.ktx)
    implementation(libs.androidX.preference)

    // Compose
    implementation(libs.bundles.androidX.compose)
    debugImplementation(libs.androidX.compose.tooling)
}