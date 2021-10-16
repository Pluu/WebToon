plugins {
    id("android-library-convention")
    id("android-compose-convention")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

dependencies {
    implementation(project(":core-android"))
    implementation(project(":ui-common"))
    implementation(project(":model"))
    implementation(project(":data"))
    implementation(project(":compose"))

    implementation(Dep.Kotlin.stdlibJvm)

    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.fragment.ktx)

    // Android UI
    implementation(Dep.AndroidX.UI.preference)

    // Compose
    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.tooling)

    implementation(Dep.Accompanist.insets)

    // Hilt
    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)
}