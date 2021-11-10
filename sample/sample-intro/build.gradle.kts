plugins {
    id("android-application-convention")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    defaultConfig {
        applicationId = "com.pluu.webtoon.intro.sample"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(projects.coreAndroid)
    implementation(projects.uiCommon)
    implementation(projects.uiIntro)

    // Hilt
    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)
}