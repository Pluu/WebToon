package buildTime.buildScan

import buildTime.MeasureBuildTimeConfig
import buildTime.buildScan.initialization.BuildInitializationService
import buildTime.lifecycle.BuildTaskService
import buildTime.providers.BuildDataProvider
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.internal.GradleInternal
import org.gradle.api.services.BuildServiceSpec
import org.gradle.build.event.BuildEventsListenerRegistry

object BuildScanner {
    fun setup(
        config: MeasureBuildTimeConfig,
        registry: BuildEventsListenerRegistry,
    ) {
        setupInitializationService(config.project)
        setupExecutionService(config.project, registry, config)
    }

    private fun setupInitializationService(project: Project) {
        project.gradle.addBuildListener(BuildInitializationService(project))

        // Gradle build listener's `projectsLoaded` function is not invoked during the build process,
        // This is an alternative solution to figure out when the initialization process has been finished.
        (project.gradle as GradleInternal).rootProject {
            BuildInitializationService.assignInitializedAt()
        }
    }

    private fun setupExecutionService(
        project: Project,
        registry: BuildEventsListenerRegistry,
        config: MeasureBuildTimeConfig,
    ) {
        project.gradle.projectsEvaluated {
            val buildTaskService = project.gradle.sharedServices.registerIfAbsent(
                BuildTaskService::class.java.simpleName,
                BuildTaskService::class.java,
                object : Action<BuildServiceSpec<BuildTaskService.Params>> {
                    override fun execute(spec: BuildServiceSpec<BuildTaskService.Params>) {
                        with(spec.parameters) {
                            enabled.set(config.enable)
                            outputPath.set(config.outputPath)
                        }
                    }
                }
            )
            registry.onTaskCompletion(buildTaskService)
        }
    }
}