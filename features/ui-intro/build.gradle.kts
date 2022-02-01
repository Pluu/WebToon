plugins {
    id("android-library-convention")
    id("android-compose-convention")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}
dependencies {
    implementation(projects.uiCommon)
    implementation(projects.compose)

    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.splashScreen)

    // Compose
    implementation(Dep.AndroidX.Compose.activity)
    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.tooling)

    implementation(Dep.Accompanist.insets)
    implementation(Dep.Accompanist.systemUi)

    // Hilt
    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)
}

kapt {
    correctErrorTypes = true
}
