package buildTime.buildScan.configuration

import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle

class BuildConfigurationService : BuildListener {

    init {
        reset()
    }

    override fun settingsEvaluated(settings: Settings) {}

    override fun projectsLoaded(gradle: Gradle) {}

    override fun projectsEvaluated(gradle: Gradle) {
        CONFIGURED_AT = System.currentTimeMillis()
    }

    override fun buildFinished(result: BuildResult) {}

    companion object {
        var CONFIGURED_AT: Long = 0L

        fun reset() {
            CONFIGURED_AT = 0L
        }

        fun assignConfiguredAt() {
            if (CONFIGURED_AT == 0L) {
                CONFIGURED_AT = System.currentTimeMillis()
            }
        }
    }
}
