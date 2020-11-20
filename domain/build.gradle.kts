plugins {
    id("com.android.library")
    kotlin("android")
}

listOf(
    "commonConfiguration.gradle",
    "libraryConfiguration.gradle"
).forEach { file ->
    apply(from = "${rootProject.projectDir}/gradle/${file}")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":model"))

    implementation(Dep.Kotlin.stdlibJvm)

    testImplementation(Dep.Test.junit)
}

//apply(from = "../publish_local.gradle")