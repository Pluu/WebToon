import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xskip-prerelease-check",
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=kotlin.Experimental",
            "-Xjvm-default=enable"
        )

        // Set JVM target
        jvmTarget = ProjectConfigurations.javaVer.toString()
    }
}