plugins {
    kotlin("plugin.serialization")
    id("pluu.android.library")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
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

    // Dagger Hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    testImplementation(libs.junit)
}

kapt {
    correctErrorTypes = true
}

//apply(from = "../publish_local.gradle")