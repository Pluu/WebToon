import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf(
//          "-Xallow-jvm-ir-dependencies",
            "-Xskip-prerelease-check",
            "-Xopt-in=kotlin.RequiresOptIn",
            // Enable experimental coroutines APIs, including Flow
//          "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
//          "-Xopt-in=kotlinx.coroutines.FlowPreview",
            "-Xopt-in=kotlin.Experimental"
        )

        // Set JVM target
        jvmTarget = ProjectConfigurations.javaVer.toString()
    }
}