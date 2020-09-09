import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsFeature

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
}

listOf(
    "commonConfiguration.gradle",
    "libraryConfiguration.gradle"
).forEach { file ->
    apply(from = "${rootProject.projectDir}/gradle/${file}")
}

dependencies {
    implementation(project(":core-android"))
    api(project(":model"))

    implementation(Dep.Kotlin.stdlibJvm)

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
