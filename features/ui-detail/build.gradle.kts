plugins {
    id("android-library-convention")
    id("android-compose-convention")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core-android"))
    implementation(project(":domain"))
    implementation(project(":site"))
    implementation(project(":ui-common"))
    implementation(project(":compose"))

    implementation(Dep.Kotlin.stdlibJvm)

    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.fragment.ktx)
    implementation(Dep.AndroidX.lifecycle.viewModelKtx)

    // Compose
    implementation(Dep.AndroidX.Compose.animation)
    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.tooling)
    implementation(Dep.AndroidX.Compose.livedata)

    implementation(Dep.Coil.compose)
    implementation(Dep.Accompanist.insets)

    // Hilt
    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    implementation(Dep.timber)
}