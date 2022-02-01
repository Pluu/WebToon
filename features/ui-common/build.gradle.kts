plugins {
    id("android-library-convention")
    id("android-compose-convention")
}

dependencies {
    api(projects.model)

    // Android UI
    implementation(Dep.AndroidX.Fragment.fragment)

    // Compose
    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.toolingPreview)
    debugImplementation(Dep.AndroidX.Compose.tooling)
    implementation(Dep.AndroidX.Compose.activity)

    // Coil
    implementation(Dep.Coil.core)
}
