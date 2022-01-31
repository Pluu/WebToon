plugins {
    id("android-library-convention")
    id("android-compose-convention")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(projects.coreAndroid)
    implementation(projects.uiCommon)
    implementation(projects.model)
    implementation(projects.domain)
    implementation(projects.compose)

    implementation(Dep.Kotlin.stdlibJvm)

    implementation(Dep.AndroidX.coreKtx)

    // Android UI
    implementation(Dep.AndroidX.UI.preference)
    implementation(Dep.AndroidX.Hilt.compose)

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
