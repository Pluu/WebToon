package buildTime.buildScan.initialization

import org.gradle.BuildResult
import org.gradle.api.Project
import org.gradle.api.ProjectEvaluationListener
import org.gradle.api.ProjectState
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.GradleInternal
import org.gradle.api.invocation.Gradle
import org.gradle.internal.InternalBuildListener
import org.gradle.internal.extensions.core.get
import org.gradle.internal.scan.time.BuildScanBuildStartedTime

class BuildInitializationService(
    private val project: Project
) : InternalBuildListener, ProjectEvaluationListener {

    init {
        reset()
    }

    override fun beforeEvaluate(project: Project) {
        assignInitializationTimestamp()
    }

    override fun afterEvaluate(project: Project, state: ProjectState) {
        assignInitializationTimestamp()
    }

    private fun assignInitializationTimestamp() {
        assignInitializedAt()
    }

    override fun settingsEvaluated(settings: Settings) {}

    override fun projectsLoaded(gradle: Gradle) {}

    override fun projectsEvaluated(gradle: Gradle) {
        STARTED_AT = getStartTimestamp()
    }

    override fun buildFinished(result: BuildResult) {}

    private fun getStartTimestamp(): Long {
        val buildStartedTimeService = (project.gradle as GradleInternal).services.get<BuildScanBuildStartedTime>()
        return buildStartedTimeService.buildStartedTime
    }

    companion object {
        var STARTED_AT: Long = 0L
        var INITIALIZED_AT: Long = 0L

        fun reset() {
            STARTED_AT = 0L
            INITIALIZED_AT = 0L
        }

        fun assignInitializedAt() {
            if (INITIALIZED_AT == 0L) {
                INITIALIZED_AT = System.currentTimeMillis()
            }
        }

        fun assignStartedAt() {
            if (STARTED_AT == 0L) {
                STARTED_AT = System.currentTimeMillis()
            }
        }
    }
}