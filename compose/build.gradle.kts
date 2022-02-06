plugins {
    id("android-library-convention")
    id("android-compose-convention")
}

dependencies {
    debugImplementation(projects.uiTheme)

    // AndroidX
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.UI.preference)
}