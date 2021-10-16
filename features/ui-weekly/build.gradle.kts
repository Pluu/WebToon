plugins {
    id("android-library-convention")
    id("android-compose-convention")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core-android"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":site"))
    implementation(project(":ui-common"))
    implementation(project(":compose"))

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

    // Hilt
    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    // Coil
    implementation(Dep.Coil.core)

    implementation(Dep.timber)
}