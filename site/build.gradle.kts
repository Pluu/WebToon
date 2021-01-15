plugins {
    kotlin("plugin.serialization")
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

listOf(
    "commonConfiguration.gradle",
    "libraryConfiguration.gradle"
).forEach { file ->
    apply(from = "${rootProject.projectDir}/gradle/${file}")
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
    implementation(Dep.Hilt.dagger_android)
    kapt(Dep.Hilt.dagger_compiler)

    testImplementation(Dep.Test.junit)
}

//apply(from = "../publish_local.gradle")