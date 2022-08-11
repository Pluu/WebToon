plugins {
    kotlin("plugin.serialization")
    id("pluu.android.library")
    id("pluu.android.hilt")
}

android {
    namespace = "com.pluu.webtoon.data.remote"
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreAndroid)
    implementation(projects.model)
    implementation(projects.data)

    implementation(libs.kotlin.serialization)
    implementation(libs.kotlin.coroutine.core)

    // Jsoup
    implementation(libs.jsoup)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.loggingInterceptor)

    testImplementation(libs.junit)
}

//apply(from = "../publish_local.gradle")