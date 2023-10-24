plugins {
    id("pluu.android.application")
    id("pluu.android.application.compose")
    id("pluu.android.hilt")
}

android {
    namespace = "com.pluu.webtoon.weekly.sample"

    defaultConfig {
        applicationId = "com.pluu.webtoon.weekly.sample"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(projects.sampleCommon)
    implementation(projects.uiWeekly)

    // Compose
    implementation(libs.androidX.activity.compose)
}