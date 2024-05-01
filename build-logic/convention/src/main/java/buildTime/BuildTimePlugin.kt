package buildTime

import buildTime.lifecycle.BuildFinishedFlowAction
import buildTime.lifecycle.BuildTaskService
import buildTime.lifecycle.ConfigurationPhaseObserver
import buildTime.providers.BuildDataProvider
import org.gradle.StartParameter
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.flow.FlowProviders
import org.gradle.api.flow.FlowScope
import org.gradle.api.provider.Provider
import org.gradle.build.event.BuildEventsListenerRegistry
import org.gradle.internal.buildevents.BuildStartedTime
import org.gradle.invocation.DefaultGradle
import javax.inject.Inject
import kotlin.time.ExperimentalTime

///////////////////////////////////////////////////////////////////////////
// Origin : https://github.com/Automattic/measure-builds-gradle-plugin
///////////////////////////////////////////////////////////////////////////

@Suppress("UnstableApiUsage")
@ExperimentalTime
class BuildTimingPlugin @Inject constructor(
    private val registry: BuildEventsListenerRegistry,
    private val flowScope: FlowScope,
    private val flowProviders: FlowProviders,
) : Plugin<Project> {

    override fun apply(project: Project) {
        val buildInitiatedTime =
            (project.gradle as DefaultGradle).services[BuildStartedTime::class.java].startTime
        val extension =
            project.extensions.create("measureBuilds", MeasureBuildsExtension::class.java, project)

        project.afterEvaluate {
            if (extension.enable.get() == true) {
                val configurationProvider: Provider<Boolean> = project.providers.of(
                    ConfigurationPhaseObserver::class.java
                ) { }
                ConfigurationPhaseObserver.init()

                prepareBuildData(project)
                prepareBuildFinishedAction(
                    project.gradle.startParameter,
                    buildInitiatedTime,
                    configurationProvider
                )
            }
        }

        prepareBuildTaskService(project)
//        prepareBuildScanListener(project, extension, metricsDispatcher)
    }

    private fun prepareBuildData(
        project: Project,
    ) {
        InMemoryReport.setBuildData(
            BuildDataProvider.provide(
                project,
                ">>>>",
            )
        )
    }

    private fun prepareBuildFinishedAction(
        startParameter: StartParameter,
        buildInitiatedTime: Long,
        configurationPhaseObserver: Provider<Boolean>,
    ) {
        flowScope.always(
            BuildFinishedFlowAction::class.java
        ) {
            parameters.apply {
                this.buildWorkResult.set(flowProviders.buildWorkResult)
                this.initiationTime.set(buildInitiatedTime)
                this.configurationPhaseExecuted.set(configurationPhaseObserver)
                this.startParameter.set(startParameter)
            }
        }
    }

    private fun prepareBuildTaskService(project: Project) {
        val serviceProvider: Provider<BuildTaskService> =
            project.gradle.sharedServices.registerIfAbsent(
                "taskEvents",
                BuildTaskService::class.java
            ) {
            }
        registry.onTaskCompletion(serviceProvider)
    }
}