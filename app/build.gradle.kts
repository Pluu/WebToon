plugins {
    id("pluu.android.application")
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
    implementation(projects.coreAndroid)
    implementation(projects.data)
    implementation(projects.dataLocal)
    implementation(projects.dataRemote)
    implementation(projects.uiIntro)
    implementation(projects.uiMainContainer)

    // kotlin
    implementation(libs.kotlin.coroutine.core)
    implementation(libs.kotlin.coroutine.android)

    implementation(libs.timber)
}

kapt {
    useBuildCache = true
}