plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    id("pluu.buildTime")
}

apply(from = "${rootDir}/gradle/jetifier_disable.gradle.kts")
apply(from = "${rootDir}/gradle/projectInverseDependencyGraph.gradle")

measureBuilds {
    enable = true
}

task("clean", Delete::class) {
    delete(layout.buildDirectory)
}
