plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    id("versions.checker") apply false
}

apply(from = "${rootDir}/gradle/projectInverseDependencyGraph.gradle")

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}