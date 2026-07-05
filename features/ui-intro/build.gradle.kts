plugins {
    id("pluu.android.library")
    id("pluu.android.library.compose")
    id("pluu.android.hilt")
}

android {
    namespace = "com.pluu.webtoon.intro"
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=androidx.compose.foundation.style.ExperimentalFoundationStyleApi")
    }
}

dependencies {
    implementation(projects.uiCommon)
    implementation(projects.compose)

    implementation(libs.androidX.core.ktx)
    implementation(libs.androidX.core.splashScreen)

    // Compose
    implementation(libs.androidX.activity.compose)
}
