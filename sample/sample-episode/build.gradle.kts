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
    implementation(projects.sampleCommon)
    implementation(projects.uiEpisode)

    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.UI.material)

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