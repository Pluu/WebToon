package buildTime

import buildTime.buildScan.BuildScanner
import buildTime.task.BuildTimeTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.build.event.BuildEventsListenerRegistry
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@ExperimentalTime
class BuildTimingPlugin @Inject constructor(
    private val registry: BuildEventsListenerRegistry
) : Plugin<Project> {

    override fun apply(project: Project) {
        val config = setupConfig(project)
        registerTasks(config)
        BuildScanner.setup(config, registry)
    }

    private fun setupConfig(project: Project): MeasureBuildTimeConfig {
        return project.extensions.create(
            PLUGIN_NAME,
            MeasureBuildTimeConfig::class.java,
            project
        )
    }

    private fun registerTasks(config: MeasureBuildTimeConfig) {
        BuildTimeTask.register(config)
    }

    companion object {
        const val PLUGIN_NAME = "measureBuilds"
    }
}