plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}

apply(from = "${rootDir}/gradle/jetifier_disable.gradle.kts")
apply(from = "${rootDir}/gradle/projectInverseDependencyGraph.gradle")

task("clean", Delete::class) {
    delete(layout.buildDirectory)
}
