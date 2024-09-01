package buildTime.model

import java.time.Duration

data class BuildData(
    val user: String,
    val gradleVersion: String,
    val operatingSystem: String,
    val environment: Environment,
    val isConfigurationCache: Boolean,
    val includedBuildsNames: List<String>,
    val architecture: String,
)

data class ExecutionData(
    val createdAt: Long,
    val startedAt: Long,
    val initializedAt: Long,
    val configuredAt: Long,
    val finishedAt: Long,

    val buildTime: Long,
    val failed: Boolean,
    val failure: String?,
    val executedTasks: List<MeasuredTaskInfo>,
    val requestedTasks: List<String>,
    val configurationPhaseDuration: Long,
) {
    fun getTotalDuration(): Duration {
        var remainingDuration = 0L
        val lastExecutedTask = executedTasks.maxByOrNull { it.finishedAt }
        if (lastExecutedTask != null) {
            if (finishedAt > lastExecutedTask.finishedAt) {
                remainingDuration = finishedAt - lastExecutedTask.finishedAt
            }
        }

        return getInitializationDuration() + getConfigurationDuration() +
                Duration.ofMillis(remainingDuration)
    }

    fun getInitializationDuration(): Duration {
        if (initializedAt < startedAt) return Duration.ofMillis(0)
        return Duration.ofMillis(initializedAt - startedAt)
    }

    fun getConfigurationDuration(): Duration {
        if (configuredAt < initializedAt) return Duration.ofMillis(0)
        return Duration.ofMillis(configuredAt - initializedAt)
    }
}

enum class Environment {
    IDE,
    CI,
    CMD
}