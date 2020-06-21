plugins {
    androidLibrary()
    kotlinAndroid()
}

android {
    setDefaultConfig()
    setLibraryProguard(project)

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.coreKtx)
}