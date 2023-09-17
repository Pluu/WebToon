plugins {
    id("pluu.android.library")
    id("pluu.android.library.compose")
    id("pluu.android.hilt")
}

android {
    namespace = "com.pluu.webtoon.main.container"
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreAndroid)
    implementation(projects.model)
    implementation(projects.uiCommon)
    implementation(projects.domain)
    implementation(projects.compose)

    implementation(projects.uiWeekly)
    implementation(projects.uiEpisode)
    implementation(projects.uiDetail)
    implementation(projects.uiSetting)

    // Android UI
    implementation(libs.androidX.browser)
    implementation(libs.androidX.navigation.compose)
    implementation(libs.androidX.hilt.navigation.compose)

    // Compose
    implementation(libs.accompanist.systemUi)

    implementation(libs.timber)
}
