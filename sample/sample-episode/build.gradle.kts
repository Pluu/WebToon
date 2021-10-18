plugins {
    id("android-application-convention")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    defaultConfig {
        applicationId = "com.pluu.webtoon.episode.sample"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreAndroid)
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.site)
    implementation(projects.uiCommon)
    implementation(projects.uiEpisode)

    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.UI.material)

    // OkHttp
    implementation(Dep.OkHttp.loggingInterceptor)

    // Hilt
    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    // Kotlin
    implementation(Dep.Kotlin.coroutines.android)

    implementation(Dep.timber)
}
