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

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)

    implementation(project(":core-android"))
    implementation(project(":domain"))

    implementation(Dep.AndroidX.appcompat)

    // Android UI
    implementation(Dep.AndroidX.UI.material)

    // Glide
    implementation(Dep.Glide.core)
}

androidExtensions {
    isExperimental = true
    features = setOf(AndroidExtensionsFeature.PARCELIZE.featureName)
}
