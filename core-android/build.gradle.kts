plugins {
    id("android-library-convention")
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

    implementation(libs.androidX.recyclerview)
    implementation(libs.androidX.window)

    testImplementation(libs.androidX.arch.coreTesting)
    testImplementation(libs.androidX.lifecycle.runtimeTesting)
    testImplementation(libs.junit)
}