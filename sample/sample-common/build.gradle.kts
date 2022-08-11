plugins {
    id("pluu.android.library")
    id("pluu.android.hilt")
}

android {
    namespace = "com.pluu.webtoon.sample"
}

dependencies {
    api(projects.core)
    api(projects.coreAndroid)
    implementation(projects.data)
    implementation(projects.dataLocal)
    implementation(projects.dataRemote)
    implementation(projects.domain)
    api(projects.uiCommon)

    // OkHttp
    implementation(libs.okhttp.loggingInterceptor)

    // Kotlin
    implementation(libs.kotlin.coroutine.android)

    api(libs.timber)
}