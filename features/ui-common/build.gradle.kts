plugins {
    id("android-library-convention")
    id("android-compose-convention")
}

dependencies {
    implementation(projects.coreAndroid)
    api(projects.model)

    implementation(Dep.Kotlin.stdlibJvm)

    // Android UI
    implementation(Dep.AndroidX.fragment.fragment)

    // Compose
    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.tooling)
    implementation(Dep.AndroidX.Compose.activity)

    // Coil
    implementation(Dep.Coil.core)
}
