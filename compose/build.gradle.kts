plugins {
    id("pluu.android.library")
    id("pluu.android.library.compose")
}

android {
    namespace = "com.pluu.compose"
}

dependencies {
    // AndroidX
    implementation(libs.androidX.core.ktx)
    implementation(libs.androidX.fragment.ktx)
    implementation(libs.androidX.preference)
    // Disabling to work with Alpha
//    api(platform(libs.androidX.compose.bom))
}