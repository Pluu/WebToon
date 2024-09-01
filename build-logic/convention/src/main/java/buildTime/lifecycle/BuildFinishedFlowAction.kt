//package buildTime.lifecycle
//
//import buildTime.InMemoryReport
//import buildTime.model.ExecutionData
//import buildTime.model.MeasuredTaskInfo
//import buildTime.report.html.HtmlUtils
//import org.gradle.StartParameter
//import org.gradle.api.flow.BuildWorkResult
//import org.gradle.api.flow.FlowAction
//import org.gradle.api.flow.FlowParameters
//import org.gradle.api.provider.Property
//import org.gradle.api.provider.Provider
//import org.gradle.api.services.ServiceReference
//import org.gradle.api.tasks.Input
//import java.io.File
//import kotlin.jvm.optionals.getOrNull
//import kotlin.random.Random
//
//class BuildFinishedFlowAction : FlowAction<BuildFinishedFlowAction.Parameters> {
//    interface Parameters : FlowParameters {
//
//        // This value will NOT update if project re-used configuration cache
//        // Use ONLY for calculating configuration phase duration if it was executed
//        @get:Input
//        val initiationTime: Property<Long>
//
//        @get:Input
//        val configurationPhaseExecuted: Property<Provider<Boolean>>
//
//        @get:Input
//        val buildWorkResult: Property<Provider<BuildWorkResult>>
//
//        @get:ServiceReference
//        val buildTaskService: Property<BuildTaskService>
//
//        @get:Input
//        val startParameter: Property<StartParameter>
//    }
//
//    override fun execute(parameters: Parameters) {
//        val init = parameters.initiationTime.get()
//        val buildStart = parameters.buildTaskService.get().buildStartTime
//        val finish = System.currentTimeMillis()
//
//        val result = parameters.buildWorkResult.get().get()
//        val buildPhaseDuration = finish - buildStart
//
//        val configurationTime = if (parameters.configurationPhaseExecuted.get().get()) {
//            buildStart - init
//        } else {
//            0
//        }
//
//        val executionData = ExecutionData(
//            buildTime = buildPhaseDuration + configurationTime,
//            failed = result.failure.isPresent,
//            failure = result.failure.getOrNull()?.message,
//            executedTasks = parameters.buildTaskService.get().taskInfos,
//            buildFinishedTimestamp = finish,
//            configurationPhaseDuration = configurationTime,
//            requestedTasks = parameters.startParameter.get().taskNames.toList()
//        )
//
//        InMemoryReport.setExecutionData(executionData)
//
//
//        val html = getMetricRender(
//            buildStart,
//            finish,
//            parameters.buildTaskService.get().taskInfos
//        )
//
////        println(">>> + ${html}")
//
//        File("a.html").writeText(html)
//
//
////        if (parameters.attachGradleScanId.get() == false) {
////            InMemoryMetricsReporter.report(
////                report = InMemoryReport,
////                gradleScanId = null
////            )
////        }
//    }
//
//    private fun getMetricRender(
//        buildStartTime: Long,
//        buildFinishTime: Long,
//        tasks: List<MeasuredTaskInfo>
//    ): String {
//        val renderedTemplate = HtmlUtils.getTemplate("modules-timeline-metric-template")
//
//        val tasks = tasks.groupBy {
//            it.name.substringBeforeLast(":")
//        }
//        val result = buildString {
//            appendLine("[")
//            tasks.entries.forEach { (key, value) ->
//                val color = generateRandomColor()
//                append(
//                    buildString {
//                        append("{")
//                        appendLine()
//                        append("label: \"${key}\",")
//                        appendLine()
//                        append("times: [")
//                        appendLine()
//                        value.forEach { timeline ->
//                            val duration = timeline.startTime + timeline.duration.inWholeMilliseconds
//                            if (timeline.isCached) {
//                                append(
//                                    "{ \"tooltip_label\": \"${timeline.name}(${"#,###".format(timeline.duration.inWholeMilliseconds)}ms)\", \"color\": \"$color\", \"starting_time\": ${
//                                        timeline.startTime
//                                    }, \"ending_time\": $duration },"
//                                )
//                            } else {
//                                append(
//                                    "{ \"tooltip_label\": \"${timeline.name}(${"#,###".format(timeline.duration.inWholeMilliseconds)}ms)\", \"starting_time\": ${
//                                        timeline.startTime
//                                    }, \"ending_time\": $duration },"
//                                )
//                            }
//                            appendLine()
//                        }
//                        append("]")
//                        appendLine()
//                        append("},")
//                    }
//                )
//                appendLine()
//            }
//            appendLine()
//            append("]")
//        }
//
//        val maxLabelWidth = tasks.keys.maxOf { it.length }.times(10) ?: 128
//
//        return renderedTemplate
//            .replace("%root-project-name%", "Webtoon")
//            .replace("%timelines%", result)
//            .replace("%beginning%", buildStartTime.toString())
//            .replace("%ending%", buildFinishTime.toString())
////            .replace("%datetime%", DateTimeUtils.formatToDateTime(createdAt))
//            .replace("%max-label-width%", "${maxLabelWidth}")
//    }
//
//    private fun generateRandomColor(): String {
//        val r = Integer.toHexString(Random.nextInt(255))
//        val g = Integer.toHexString(Random.nextInt(255))
//        val b = Integer.toHexString(Random.nextInt(255))
//        return "#$r$g$b"
//    }
//}