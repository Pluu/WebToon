plugins {
    id("android-library-convention")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.pluu.webtoon.episode"

    buildFeatures.viewBinding = true
    buildFeatures.compose = true

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
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
    implementation(libs.bundles.androidX.compose)
    debugImplementation(libs.bundles.androidX.compose.preview)
    implementation(libs.androidX.constraintlayout.compose)

    implementation(libs.coil.compose)
    implementation(libs.accompanist.systemUi)

    // Hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    implementation(libs.timber)
}

kapt {
    correctErrorTypes = true
}
