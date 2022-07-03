import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xskip-prerelease-check",
            "-Xjvm-default=all"
        )

        // Set JVM target
        jvmTarget = ProjectConfigurations.javaVer.toString()
    }
}