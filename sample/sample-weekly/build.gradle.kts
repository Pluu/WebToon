plugins {
    id("android-application-convention")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    defaultConfig {
        applicationId = "com.pluu.webtoon.weekly.sample"
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures.compose = true

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
}

dependencies {
    implementation(projects.sampleCommon)
    implementation(projects.uiWeekly)

    // Compose
    implementation(libs.bundles.androidX.compose)
    debugImplementation(libs.androidX.compose.tooling)
    implementation(libs.androidX.activity.compose)

    implementation(libs.accompanist.systemUi)

    // Hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
}

kapt {
    correctErrorTypes = true
}

hilt {
    enableAggregatingTask = true
}