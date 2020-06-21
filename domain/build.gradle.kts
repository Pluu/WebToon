import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsFeature

plugins {
    androidLibrary()
    kotlinAndroid()
    kotlinAndroidExtensions()
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

    testImplementation(Dep.Test.junit)
}

//apply(from = "../publish_local.gradle")