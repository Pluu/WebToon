import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    addScriptRepository()
    addScriptDependencies()
}

allprojects {
    addScriptRepository()

    tasks.withType(KotlinCompile::class).configureEach {
        kotlinOptions {
            useIR = true

            freeCompilerArgs = freeCompilerArgs + listOf(
                "-Xallow-jvm-ir-dependencies",
                "-Xskip-prerelease-check",
                "-Xopt-in=kotlin.RequiresOptIn",
                // Enable experimental coroutines APIs, including Flow
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xopt-in=kotlinx.coroutines.FlowPreview",
                "-Xopt-in=kotlin.Experimental"
            )

            // Set JVM target to 1.8
            jvmTarget = ProjectConfigurations.javaVerString
        }
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
