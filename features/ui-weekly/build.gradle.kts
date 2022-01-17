plugins {
    id("android-library-convention")
    id("android-compose-convention")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreAndroid)
    implementation(projects.domain)
    implementation(projects.site)
    implementation(projects.uiCommon)
    implementation(projects.compose)

    implementation(Dep.Kotlin.stdlibJvm)

    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.fragment.ktx)
    implementation(Dep.AndroidX.lifecycle.viewModelKtx)

    // Android UI
    implementation(Dep.AndroidX.UI.palette)

    // Compose
    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.tooling)
    implementation(Dep.AndroidX.Compose.livedata)
    implementation(Dep.AndroidX.Compose.activity)
    implementation(Dep.AndroidX.Compose.constraintLayout)
    implementation(Dep.AndroidX.Compose.viewModel)

    implementation(Dep.Coil.compose)
    implementation(Dep.Accompanist.insets)
    implementation(Dep.Accompanist.pager)
    implementation(Dep.Accompanist.pagerIndicators)

    // Hilt
    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    // Coil
    implementation(Dep.Coil.core)

    implementation(Dep.timber)
}

kapt {
    correctErrorTypes = true
}
