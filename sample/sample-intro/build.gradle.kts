plugins {
    id("pluu.android.application")
    id("pluu.android.hilt")
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
}