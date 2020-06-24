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
    implementation(project(":core"))
    implementation(project(":core-android"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":model"))

    implementation(Dep.Kotlin.stdlibJvm)
    implementation(Dep.Kotlin.serialization)
    implementation(Dep.Kotlin.coroutines.core)
    // Jsoup
    implementation(Dep.jsoup)

    // Dagger Hilt
    implementation(Dep.Hilt.android)
    kapt(Dep.Hilt.hilt_compiler)

    testImplementation(Dep.Test.junit)
}

//apply(from = "../publish_local.gradle")