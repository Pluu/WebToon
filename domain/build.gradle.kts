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
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":model"))

    implementation(Dep.Kotlin.stdlibJvm)

    testImplementation(Dep.Test.junit)
}

//apply(from = "../publish_local.gradle")