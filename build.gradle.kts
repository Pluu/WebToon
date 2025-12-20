plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    id("versions.loader")
    id("versions.checker") apply false
}

apply(from = "${rootDir}/gradle/jetifier_disable.gradle.kts")
apply(from = "${rootDir}/gradle/projectInverseDependencyGraph.gradle")

subprojects {
    project.apply {
        plugin("versions.checker")
    }

    // TODO: https://github.com/google/dagger/issues/5001
    configurations.configureEach {
        resolutionStrategy.eachDependency {
            if (requested.name.startsWith("kotlin-metadata-jvm")) {
                useVersion(libs.versions.kotlin.asProvider().get())
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}