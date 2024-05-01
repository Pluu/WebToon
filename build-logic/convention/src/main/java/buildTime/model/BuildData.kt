package buildTime.model

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
    val buildTime: Long,
    val failed: Boolean,
    val failure: String?,
    val executedTasks: List<MeasuredTask>,
    val requestedTasks: List<String>,
    val buildFinishedTimestamp: Long,
    val configurationPhaseDuration: Long,
)

enum class Environment {
    IDE,
    CI,
    CMD
}