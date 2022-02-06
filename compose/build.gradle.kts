plugins {
    id("android-library-convention")
    id("android-compose-convention")
}

dependencies {
    // AndroidX
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.UI.preference)
}