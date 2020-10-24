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

android {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerVersion = Dep.Kotlin.version
        kotlinCompilerExtensionVersion = Dep.AndroidX.Compose.version
    }
}

dependencies {
    implementation(project(":core-android"))
    api(project(":model"))

    implementation(Dep.Kotlin.stdlibJvm)

    implementation(Dep.AndroidX.appcompat)

    // Android UI
    implementation(Dep.AndroidX.UI.material)

    // Compose
    implementation(Dep.AndroidX.Compose.runtime)
    implementation(Dep.AndroidX.Compose.foundation)
    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.layout)
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.materialAdapter)
    implementation(Dep.AndroidX.Compose.tooling)
    implementation(Dep.AndroidX.Compose.livedata)

    // Glide
    implementation(Dep.Glide.core)
}

androidExtensions {
    isExperimental = true
    features = setOf(AndroidExtensionsFeature.PARCELIZE.featureName)
}
