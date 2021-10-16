buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/") {
            content {
                includeGroup("org.jlleitschuh.gradle")
            }
        }
    }
    dependencies {
        classpath(Dep.GradlePlugin.android)
        classpath(Dep.GradlePlugin.kotlin)
        classpath(Dep.GradlePlugin.kotlinSerialization)
        classpath(Dep.GradlePlugin.ktlint)
        classpath(Dep.GradlePlugin.hilt)
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
