plugins {
    id("pluu.android.application")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.pluu.webtoon.intro.sample"

    defaultConfig {
        applicationId = "com.pluu.webtoon.intro.sample"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(projects.sampleCommon)
    implementation(projects.uiIntro)

    // Hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
}

kapt {
    correctErrorTypes = true
}

hilt {
    enableAggregatingTask = true
}