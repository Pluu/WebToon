plugins {
    id("pluu.android.application")
    id("pluu.android.application.compose")
    id("pluu.android.hilt")
}

android {
    namespace = "com.pluu.webtoon.detail.sample"

    defaultConfig {
        applicationId = "com.pluu.webtoon.detail.sample"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(projects.sampleCommon)
    implementation(projects.uiDetail)

    // Compose
    implementation(libs.androidX.activity.compose)

    implementation(libs.accompanist.systemUi)
}