package buildTime.task

import buildTime.MeasureBuildTimeConfig
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class BuildTimeTask : DefaultTask() {

    @get:Input
    abstract val outputPathProperty: Property<String>

    @TaskAction
    fun execute() = runBlocking {
        println(">>>>>")
    }

    companion object {
        const val TASK_NAME = "reportAnalytics"
        private val clazz = BuildTimeTask::class.java

        fun register(config: MeasureBuildTimeConfig) {
            val task = config.project.tasks.create(TASK_NAME, BuildTimeTask::class.java)
            config.project.gradle.projectsEvaluated {
                with(task) {
                    outputPathProperty.set(config.outputPath)
                }
            }
        }
    }
}