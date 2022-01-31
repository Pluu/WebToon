plugins {
    id("android-application-convention")
    id("android-compose-convention")
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
    implementation(projects.sampleCommon)
    implementation(projects.uiEpisode)

    // Compose
    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.foundation)
    implementation(Dep.AndroidX.Compose.runtime)
    implementation(Dep.AndroidX.Compose.activity)

    implementation(Dep.Accompanist.insets)
    implementation(Dep.Accompanist.systemUi)

    // Hilt
    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)
}

kapt {
    correctErrorTypes = true
}

hilt {
    enableAggregatingTask = true
}