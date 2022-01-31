plugins {
    id("android-library-convention")
    id("android-compose-convention")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(projects.uiCommon)
    implementation(projects.model)
    implementation(projects.domain)
    implementation(projects.compose)

    implementation(Dep.Kotlin.stdlibJvm)

    // AndroidX
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.Hilt.compose)
    implementation(Dep.AndroidX.UI.preference)

    // Compose
    implementation(Dep.AndroidX.Compose.activity)
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.tooling)
    implementation(Dep.AndroidX.Compose.ui)

    implementation(Dep.Accompanist.insets)
    implementation(Dep.Accompanist.systemUi)

    // Hilt
    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)
}

kapt {
    correctErrorTypes = true
}
