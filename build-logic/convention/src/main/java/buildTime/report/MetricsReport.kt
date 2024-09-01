package buildTime.report

import buildTime.buildScan.initialization.BuildInitializationService
import buildTime.lifecycle.BuildTaskService
import buildTime.model.ExecutionData
import buildTime.model.MeasuredTaskInfo
import buildTime.report.html.HtmlUtils
import java.io.File
import java.text.DecimalFormat
import kotlin.random.Random

interface MetricsReport {
    fun onExecutionFinished(executedTasks: Collection<MeasuredTaskInfo>)
}

class MetricsReportImpl(
    private val params: BuildTaskService.Params,
) : MetricsReport {

    override fun onExecutionFinished(executedTasks: Collection<MeasuredTaskInfo>) {
        // TODO: 데이터 갱신
        val executionData = ExecutionData(
            buildTime = 0 /*buildPhaseDuration + configurationTime*/,
            failed = executedTasks.none { it.isSuccessful },
            failure = executedTasks.mapNotNull { it.failures }.joinToString(),
            executedTasks = executedTasks.toList(),
            buildFinishedTimestamp = System.currentTimeMillis(),
            configurationPhaseDuration = 0,
            requestedTasks = emptyList()
        )

        val html = getMetricRender(
            BuildInitializationService.STARTED_AT,
            executionData.buildFinishedTimestamp,
            executedTasks
        )

        File(params.outputPath.get(), "metrics_report.html").writeText(html)
    }

    private fun getMetricRender(
        buildStartTime: Long,
        buildFinishTime: Long,
        tasks: Collection<MeasuredTaskInfo>
    ): String {
        val renderedTemplate = HtmlUtils.getTemplate("modules-timeline-metric-template")

        val tasks = tasks.groupBy {
            it.name.substringBeforeLast(":")
        }
        val df = DecimalFormat("#,###")

        val result = buildString {
            appendLine("[")
            tasks.entries.forEach { (key, value) ->
                val color = generateRandomColor()
                append(
                    buildString {
                        append("{")
                        appendLine()
                        append("label: \"${key}\",")
                        appendLine()
                        append("times: [")
                        appendLine()
                        value.forEach { timeline ->
                            val duration =
                                timeline.startTime + timeline.duration.inWholeMilliseconds
                            if (timeline.isCached) {
                                append(
                                    "{ \"tooltip_label\": \"${timeline.name}(${
                                        df.format(timeline.duration.inWholeMilliseconds)
                                    }ms)\", \"color\": \"$color\", \"starting_time\": ${
                                        timeline.startTime
                                    }, \"ending_time\": $duration },"
                                )
                            } else {
                                append(
                                    "{ \"tooltip_label\": \"${timeline.name}(${
                                        df.format(timeline.duration.inWholeMilliseconds)
                                    }ms)\", \"starting_time\": ${
                                        timeline.startTime
                                    }, \"ending_time\": $duration },"
                                )
                            }
                            appendLine()
                        }
                        append("]")
                        appendLine()
                        append("},")
                    }
                )
                appendLine()
            }
            appendLine()
            append("]")
        }

        val maxLabelWidth = tasks.keys.maxOf { it.length }.times(10) ?: 128

        return renderedTemplate
            .replace("%root-project-name%", "Webtoon")
            .replace("%timelines%", result)
            .replace("%beginning%", buildStartTime.toString())
            .replace("%ending%", buildFinishTime.toString())
//            .replace("%datetime%", DateTimeUtils.formatToDateTime(createdAt))
            .replace("%max-label-width%", "${maxLabelWidth}")
    }

    private fun generateRandomColor(): String {
        val r = Integer.toHexString(Random.nextInt(255))
        val g = Integer.toHexString(Random.nextInt(255))
        val b = Integer.toHexString(Random.nextInt(255))
        return "#$r$g$b"
    }
}