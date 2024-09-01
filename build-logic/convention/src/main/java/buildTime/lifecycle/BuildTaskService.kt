package buildTime.lifecycle

import buildTime.model.MeasuredTaskInfo
import buildTime.model.MeasuredTaskInfo.State.EXECUTED
import buildTime.model.MeasuredTaskInfo.State.INCREMENTAL
import buildTime.model.MeasuredTaskInfo.State.IS_FROM_CACHE
import buildTime.model.MeasuredTaskInfo.State.UP_TO_DATE
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.tooling.events.FinishEvent
import org.gradle.tooling.events.OperationCompletionListener
import org.gradle.tooling.events.SuccessResult
import org.gradle.tooling.events.task.TaskFinishEvent
import org.gradle.tooling.events.task.TaskSuccessResult
import kotlin.time.Duration.Companion.milliseconds

abstract class BuildTaskService :
    BuildService<BuildServiceParameters.None>,
    OperationCompletionListener {

    private val measuredTaskInfos = mutableListOf<MeasuredTaskInfo>()

    val taskInfos: List<MeasuredTaskInfo>
        get() = measuredTaskInfos

    val buildStartTime: Long = System.currentTimeMillis()

    override fun onFinish(event: FinishEvent?) {
        if (event is TaskFinishEvent) {
            if (event.result is SuccessResult) {
                val result = event.result as TaskSuccessResult

                measuredTaskInfos.add(
                    MeasuredTaskInfo(
                        name = event.descriptor?.name.toString(),
                        startTime = event.result.startTime,
                        duration = (event.result.endTime - event.result.startTime).milliseconds,
                        state = when {
                            result.isFromCache -> IS_FROM_CACHE
                            result.isUpToDate -> UP_TO_DATE
                            result.isIncremental -> INCREMENTAL
                            else -> EXECUTED
                        }
                    )
                )
            }
        }
    }
}