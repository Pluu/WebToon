plugins {
    id("pluu.android.library")
    id("pluu.android.library.compose")
    id("pluu.android.hilt")
}

android {
    namespace = "com.pluu.webtoon.setting"
}

dependencies {
    implementation(projects.uiCommon)
    implementation(projects.model)
    implementation(projects.domain)
    implementation(projects.compose)

    // AndroidX
    implementation(libs.androidX.core.ktx)
    implementation(libs.androidX.hilt.navigation.compose)
    implementation(libs.androidX.fragment.ktx)
    implementation(libs.androidX.preference)

    // Compose
    implementation(libs.androidX.activity.compose)

    implementation(libs.accompanist.systemUi)
}
