import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

allprojects {
    tasks.withType<JavaCompile> {
        sourceCompatibility = ProjectConfigurations.javaVer.majorVersion
        targetCompatibility = ProjectConfigurations.javaVer.majorVersion
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf(
//                "-Xallow-jvm-ir-dependencies",
                "-Xskip-prerelease-check",
                "-Xopt-in=kotlin.RequiresOptIn",
                // Enable experimental coroutines APIs, including Flow
//                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
//                "-Xopt-in=kotlinx.coroutines.FlowPreview",
                "-Xopt-in=kotlin.Experimental"
            )

            // Set JVM target to Java 1.8
            jvmTarget = JavaVersion.VERSION_1_8.majorVersion
        }
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
