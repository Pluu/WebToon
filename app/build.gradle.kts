plugins {
    id("pluu.android.application")
    id("pluu.android.application.compose")
    id("pluu.android.hilt")
}

android {
    namespace = "com.pluu.webtoon"

    defaultConfig {
        applicationId = "com.pluu.webtoon"
        versionCode = 71
        versionName = "1.7.4"
    }

    useLibrary("android.test.mock")
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreAndroid)
    implementation(projects.model)
    implementation(projects.data)
    implementation(projects.dataLocal)
    implementation(projects.dataRemote)
    implementation(projects.domain)
    implementation(projects.compose)
    implementation(projects.uiCommon)
    implementation(projects.uiIntro)
    implementation(projects.uiMainContainer)

    // Android UI
    implementation(libs.androidX.browser)
    implementation(libs.androidX.navigation.compose)
    implementation(libs.androidX.hilt.navigation.compose)

    // Compose
    implementation(libs.coil.compose)
    implementation(libs.accompanist.systemUi)

    // kotlin
    implementation(libs.kotlin.coroutine.core)
    implementation(libs.kotlin.coroutine.android)

    implementation(libs.timber)
    implementation(libs.coil.core)

    testImplementation(libs.junit)
    testImplementation(libs.assertJ)
    testImplementation(libs.mockito)
}

kapt {
    useBuildCache = true
}