import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsFeature

plugins {
    androidLibrary()
    kotlinAndroid()
    kotlinAndroidExtensions()
    daggerHilt()
    kotlinKapt()
}

android {
    setDefaultConfig()
    setLibraryProguard(project)
}

androidExtensions {
    isExperimental = true
    features = setOf(AndroidExtensionsFeature.PARCELIZE.featureName)
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)

    implementation(project(":core"))
    implementation(project(":data"))

    // Dagger Hilt
    implementation(Dep.Dagger.android)
    kapt(Dep.Dagger.hilt_compiler)

    testImplementation(Dep.Test.junit)
}

apply(from = "../publish_local.gradle")