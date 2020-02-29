plugins {
    androidLibrary()
    kotlinAndroid()
    kotlinAndroidExtensions()
}

android {
    setDefaultConfig()
    useDefaultBuildTypes()
}

dependencies {
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(Dep.Kotlin.stdlibJvm)
    testImplementation(Dep.Test.junit)
}

androidExtensions {
    isExperimental = true
}
