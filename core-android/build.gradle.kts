plugins {
    id("pluu.android.library")
}

android {
    namespace = "com.pluu.webtoon.core"
}

dependencies {
    implementation(libs.androidX.activity.ktx)
    implementation(libs.androidX.appcompat)
    implementation(libs.androidX.core.ktx)
    implementation(libs.androidX.fragment)
    compileOnly(libs.androidX.viewBinding)

    testImplementation(libs.androidX.arch.coreTesting)
    testImplementation(libs.androidX.lifecycle.runtimeTesting)
    testImplementation(libs.junit)
}