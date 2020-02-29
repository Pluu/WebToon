plugins {
    androidLibrary()
    kotlinAndroid()
}

android {
    setDefaultConfig()
    useDefaultBuildTypes()
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)
}
