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
    testImplementation(Dep.Test.junit)
}

apply(from = "../publish_local.gradle")