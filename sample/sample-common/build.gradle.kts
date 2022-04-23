plugins {
    id("android-library-convention")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
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

    // Hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    // OkHttp
    implementation(libs.okhttp.loggingInterceptor)

    // Kotlin
    implementation(libs.kotlin.coroutine.android)

    api(libs.timber)
}

kapt {
    correctErrorTypes = true
}

hilt {
    enableAggregatingTask = true
}
