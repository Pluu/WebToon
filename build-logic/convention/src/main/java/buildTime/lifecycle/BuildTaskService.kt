package buildTime.lifecycle

import buildTime.model.MeasuredTaskInfo
import buildTime.model.MeasuredTaskInfo.State.EXECUTED
import buildTime.model.MeasuredTaskInfo.State.INCREMENTAL
import buildTime.model.MeasuredTaskInfo.State.IS_FROM_CACHE
import buildTime.model.MeasuredTaskInfo.State.UP_TO_DATE
import buildTime.report.MetricsReport
import buildTime.report.MetricsReportImpl
import org.gradle.api.provider.Property
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.tooling.Failure
import org.gradle.tooling.events.FailureResult
import org.gradle.tooling.events.FinishEvent
import org.gradle.tooling.events.OperationCompletionListener
import org.gradle.tooling.events.SkippedResult
import org.gradle.tooling.events.SuccessResult
import org.gradle.tooling.events.task.TaskFinishEvent
import org.gradle.tooling.events.task.TaskSuccessResult
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.time.Duration.Companion.milliseconds

abstract class BuildTaskService : BuildService<BuildTaskService.Params>,
    OperationCompletionListener,
    AutoCloseable {

    interface Params : BuildServiceParameters {
        val enabled: Property<Boolean>
        val outputPath: Property<String>
    }

    private val report: MetricsReport = MetricsReportImpl(parameters)

    private val measuredTaskInfos = ConcurrentLinkedQueue<MeasuredTaskInfo>()

    override fun onFinish(event: FinishEvent?) {
        if (event is TaskFinishEvent) {
            var isSuccessful = false
            var failures: List<Failure>? = null

            when (val result = event.result) {
                is SuccessResult -> {
                    isSuccessful = true
                }

                is FailureResult -> {
                    failures = result.failures
                }

                is SkippedResult -> {
                    isSuccessful = true
                }
            }

            if (event.result is SuccessResult) {
                val result = event.result as TaskSuccessResult

                measuredTaskInfos.add(
                    MeasuredTaskInfo(
                        isSuccessful = isSuccessful,
                        failures = failures,
                        name = event.descriptor?.name.toString(),
                        startTime = event.result.startTime,
                        finishedAt = event.result.endTime,
                        duration = (event.result.endTime - event.result.startTime).milliseconds,
                        state = when {
                            result.isFromCache -> IS_FROM_CACHE
                            result.isUpToDate -> UP_TO_DATE
                            result.isIncremental -> INCREMENTAL
                            else -> EXECUTED
                        },
                    )
                )
            }
        }
    }

    override fun close() {
        report.onExecutionFinished(measuredTaskInfos)
        measuredTaskInfos.clear()
    }
}