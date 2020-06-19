plugins {
    kotlin("plugin.serialization")
    androidLibrary()
    kotlinAndroid()
    daggerHilt()
    kotlinKapt()
}

android {
    setDefaultConfig()
    setLibraryProguard(project)
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.Kotlin.serialization)

    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(Dep.Kotlin.coroutines.core)
    // Jsoup
    implementation(Dep.jsoup)

    // Dagger Hilt
    implementation(Dep.Hilt.android)
    kapt(Dep.Hilt.hilt_compiler)
    implementation(Dep.Hilt.hilt_common)

    testImplementation(Dep.Test.junit)
}

apply(from = "../publish_local.gradle")