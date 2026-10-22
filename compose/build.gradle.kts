plugins {
    id("pluu.android.library")
    id("pluu.android.library.compose")
}

android {
    namespace = "com.pluu.compose"
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=androidx.compose.foundation.style.ExperimentalFoundationStyleApi")
    }
}

dependencies {
    // AndroidX
    implementation(libs.androidX.core.ktx)
    implementation(libs.androidX.fragment.ktx)
    implementation(libs.androidX.preference)
    // Disabling to work with Alpha
//    api(platform(libs.androidX.compose.bom))
}