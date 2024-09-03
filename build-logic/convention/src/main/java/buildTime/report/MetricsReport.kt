package buildTime.report

import buildTime.buildScan.configuration.BuildConfigurationService
import buildTime.buildScan.initialization.BuildInitializationService
import buildTime.extenstions.toTimeStamp
import buildTime.lifecycle.BuildTaskService
import buildTime.model.ExecutionData
import buildTime.model.MeasuredTaskInfo
import buildTime.model.Module
import buildTime.report.html.HtmlUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path
import kotlin.random.Random

interface MetricsReport {
    fun onExecutionFinished(executedTasks: Collection<MeasuredTaskInfo>)
}

class MetricsReportImpl(
    private val params: BuildTaskService.Params,
) : MetricsReport {

    init {
        BuildInitializationService.assignStartedAt()
        BuildInitializationService.assignInitializedAt()
        BuildConfigurationService.assignConfiguredAt()
    }

    override fun onExecutionFinished(executedTasks: Collection<MeasuredTaskInfo>) {
        val init = BuildInitializationService.INITIALIZED_AT
        val buildStart = BuildInitializationService.STARTED_AT
        val finish = System.currentTimeMillis()
        val buildPhaseDuration = finish - buildStart
        val configurationTime = buildStart - init

        val executionData = ExecutionData(
            createdAt = System.currentTimeMillis(),
            startedAt = BuildInitializationService.STARTED_AT,
            initializedAt = BuildInitializationService.INITIALIZED_AT,
            configuredAt = BuildConfigurationService.CONFIGURED_AT,
            finishedAt = System.currentTimeMillis(),

            buildTime = buildPhaseDuration + configurationTime,
            isSuccessful = executedTasks.all { it.isSuccessful },
            failure = executedTasks.mapNotNull { it.failures }.flatten(),
            executedTasks = executedTasks.toList(),
            configurationPhaseDuration = configurationTime,
            requestedTasks = emptyList()
        )

        val html = getMetricRender(
            executionData,
            params.modules.get()
        )

        saveReport(html)
    }

    private fun getMetricRender(
        buildData: ExecutionData,
        modules: Set<Module>
    ): String {
        val renderedTemplate = HtmlUtils.getTemplate("modules-timeline-metric-template")

        val groupingTasks = modules.map {
            it to buildData.executedTasks.filter { task ->
                task.path.startsWith(it.path)
            }
        }
        val result = buildString {
            appendLine("[")

            groupingTasks.forEach {
                val color = generateRandomColor()
                append(
                    buildString {
                        append("{")
                        appendLine()
                        append("label: \"${it.first.path.substringAfterLast(":")}\",")
                        appendLine()
                        append("times: [")
                        appendLine()
                        it.second.forEach { timeline ->
                            val duration =
                                timeline.startTime + timeline.duration.inWholeMilliseconds
                            append(
                                "{ \"tooltip_label\": \"${timeline.name}(${
                                    timeline.duration.toTimeStamp()
                                })\", \"color\": \"$color\", \"starting_time\": ${
                                    timeline.startTime
                                }, \"ending_time\": $duration },"
                            )
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

        val maxLabelWidth = groupingTasks.maxOf { it.first.path.length }.times(10) ?: 128

        val timeInfo = if (buildData.isSuccessful) {
            """
        Process duration
        <ul>
            <li>Initialization : ${buildData.getInitializationDuration().toTimeStamp()}</li>
            <li>Configuration : ${buildData.getConfigurationDuration().toTimeStamp()}</li>
            <li>Build execution : ${buildData.getBuildExecutionDuration().toTimeStamp()}</li>
            <li>Total : ${buildData.getTotalDuration().toTimeStamp()}</li>
        </ul>
        """.trimIndent()
        } else {
            ""
        }

        return renderedTemplate
            .replace("%root-project-name%", "Webtoon")
            .replace("%timelines%", result)
            .replace("%timeinfo%", timeInfo)
            .replace(
                "%beginning%",
                (buildData.executedTasks.minOfOrNull { it.startTime } ?: 0).toString())
            .replace(
                "%ending%",
                (buildData.executedTasks.maxOfOrNull { it.finishedAt } ?: 0).toString())
//            .replace("%datetime%", DateTimeUtils.formatToDateTime(createdAt))
            .replace("%max-label-width%", "${maxLabelWidth}")
    }

    private fun generateRandomColor(): String {
        val r = Integer.toHexString(Random.nextInt(255))
        val g = Integer.toHexString(Random.nextInt(255))
        val b = Integer.toHexString(Random.nextInt(255))
        return "#$r$g$b"
    }

    private fun saveReport(html: String) {
        val outputPath = params.outputPath.get()

        val outputResPat = Path("$outputPath/res")
        if (!Files.isDirectory(outputResPat)) {
            Files.createDirectory(outputResPat)
        }

        listOf(
            "chart.js",
            "d3.js",
            "functions.js",
            "jetbrainsmono-regular.ttf",
            "jquery.js",
            "mermaid.js",
            "opensans-regular.ttf",
            "panzoom.js",
            "plugin-logo.png",
            "styles.css",
            "timeline.js",
        ).forEach { resource ->
            Files.copy(
                javaClass.getResourceAsStream("/res/${resource}"),
                Path("$outputPath/res/${resource}"),
                StandardCopyOption.REPLACE_EXISTING
            )
        }

        File(outputPath, "metrics_report.html").writeText(html)
    }
}