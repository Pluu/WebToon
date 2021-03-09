import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    addScriptRepository()
    addScriptDependencies()
}

allprojects {
    tasks.withType<JavaCompile> {
        sourceCompatibility = ProjectConfigurations.javaVer.majorVersion
        targetCompatibility = ProjectConfigurations.javaVer.majorVersion
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            useIR = true

            freeCompilerArgs = freeCompilerArgs + listOf(
//                "-Xallow-jvm-ir-dependencies",
                "-Xskip-prerelease-check",
                "-Xopt-in=kotlin.RequiresOptIn",
                // Enable experimental coroutines APIs, including Flow
//                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
//                "-Xopt-in=kotlinx.coroutines.FlowPreview",
                "-Xopt-in=kotlin.Experimental"
            )

            // Set JVM target to Java 11
            jvmTarget = ProjectConfigurations.javaVer.majorVersion
        }
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
