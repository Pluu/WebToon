plugins {
    id("android-library-convention")
    id("android-compose-convention")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}
dependencies {
    implementation(projects.coreAndroid)
    implementation(projects.uiCommon)
    implementation(projects.compose)

    implementation(Dep.Kotlin.stdlibJvm)

    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.activity.ktx)

    // Compose
    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.tooling)

    // Hilt
    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)
}