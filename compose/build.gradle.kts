plugins {
    id("android-library-convention")
    id("android-compose-convention")
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)

    // AndroidX
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.UI.preference)

    // Compose
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.tooling)
}