package buildTime.lifecycle

import buildTime.model.MeasuredTask
import buildTime.model.MeasuredTask.State.EXECUTED
import buildTime.model.MeasuredTask.State.IS_FROM_CACHE
import buildTime.model.MeasuredTask.State.UP_TO_DATE
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.tooling.events.FinishEvent
import org.gradle.tooling.events.OperationCompletionListener
import org.gradle.tooling.events.SuccessResult
import org.gradle.tooling.events.task.TaskFinishEvent
import org.gradle.tooling.events.task.TaskSuccessResult

abstract class BuildTaskService :
    BuildService<BuildServiceParameters.None>,
    OperationCompletionListener {

    private val measuredTasks = mutableListOf<MeasuredTask>()

    val tasks: List<MeasuredTask>
        get() = measuredTasks

    val buildStartTime: Long = System.currentTimeMillis()

    override fun onFinish(event: FinishEvent?) {
        if (event is TaskFinishEvent) {
            if (event.result is SuccessResult) {
                val result = event.result as TaskSuccessResult

                measuredTasks.add(
                    MeasuredTask(
                        name = event.descriptor?.name.toString(),
                        startTime = event.result.startTime,
                        endTime = event.result.endTime,
                        state = when {
                            result.isFromCache -> IS_FROM_CACHE
                            result.isUpToDate -> UP_TO_DATE
                            else -> EXECUTED
                        }
                    )
                )
            }
        }
    }
}