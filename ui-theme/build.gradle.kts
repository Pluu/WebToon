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
    // Compose
    implementation(libs.bundles.androidX.compose)
    debugImplementation(libs.androidX.compose.tooling)
}