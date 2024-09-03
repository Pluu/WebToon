package buildTime.model

import org.gradle.tooling.Failure
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

data class ExecutionData(
    val createdAt: Long,
    val startedAt: Long,
    val initializedAt: Long,
    val configuredAt: Long,
    val finishedAt: Long,
    val buildTime: Long,
    val isSuccessful: Boolean,
    val failure: List<Failure>,
    val executedTasks: List<MeasuredTaskInfo>,
    val requestedTasks: List<String>,
    val configurationPhaseDuration: Long
) {
    fun getTotalDuration(): Duration {
        return getInitializationDuration() + getConfigurationDuration() + getBuildExecutionDuration()
    }

    fun getInitializationDuration(): Duration {
        if (initializedAt < startedAt) return 0.milliseconds
        return (initializedAt - startedAt).milliseconds
    }

    fun getConfigurationDuration(): Duration {
        if (configuredAt < initializedAt) return 0.milliseconds
        return (configuredAt - initializedAt).milliseconds
    }

    fun getBuildExecutionDuration(): Duration {
        val firstStartTask = executedTasks.minOfOrNull { it.startTime } ?: 0
        val lastExecutedTask = executedTasks.maxOfOrNull { it.finishedAt } ?: 0
        return (lastExecutedTask - firstStartTask).milliseconds
    }
}
