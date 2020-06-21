plugins {
    androidLibrary()
    kotlinAndroid()
}

android {
    setDefaultConfig()
    setLibraryProguard(project)
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.coreKtx)
    compileOnly(Dep.AndroidX.viewBinding)
}