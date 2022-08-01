plugins {
    id("pluu.android.application")
    id("pluu.android.application.compose")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.pluu.webtoon.episode.sample"

    defaultConfig {
        applicationId = "com.pluu.webtoon.episode.sample"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(projects.sampleCommon)
    implementation(projects.uiEpisode)

    // Compose
    implementation(libs.bundles.androidX.compose)
    debugImplementation(libs.bundles.androidX.compose.preview)
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