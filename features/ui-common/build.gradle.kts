plugins {
    id("android-library-convention")
    id("android-compose-convention")
}

dependencies {
    api(projects.model)
    api(projects.uiTheme)

    // Android UI
    implementation(Dep.AndroidX.Fragment.fragment)

    // Compose
    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.activity)

    // Coil
    implementation(Dep.Coil.core)
}
