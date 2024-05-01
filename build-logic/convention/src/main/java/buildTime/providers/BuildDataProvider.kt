package buildTime.providers

import buildTime.model.BuildData
import buildTime.model.Environment
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle

object BuildDataProvider {

    fun provide(
        project: Project,
        username: String,
    ): BuildData {
        val gradle = project.gradle
        val startParameter = gradle.startParameter

        @Suppress("UnstableApiUsage")
        return BuildData(
            environment = gradle.environment(),
            gradleVersion = gradle.gradleVersion,
            operatingSystem = System.getProperty("os.name").lowercase(),
            isConfigurationCache = startParameter.isConfigurationCacheRequested,
            includedBuildsNames = gradle.includedBuilds.toList().map { it.name },
            architecture = architecture(project),
            user = username,
        )
    }

    private fun Gradle.environment(): Environment {
        return when {
            rootProject.hasProperty("android.injected.invoked.from.ide") -> Environment.IDE
            System.getenv("CI") != null -> Environment.CI
            else -> Environment.CMD
        }
    }

    private fun architecture(project: Project): String {
        val exec = project.providers.exec {
            commandLine("uname", "-m")
        }.standardOutput.asText.get()
        return exec.trim()
    }
}