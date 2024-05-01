package buildTime

import buildTime.model.BuildData
import buildTime.model.ExecutionData

object InMemoryReport {
    private var buildDataStore: BuildData? = null
    private var executionDataStore: ExecutionData? = null

    fun setBuildData(buildData: BuildData) {
        buildDataStore = buildData
    }

    fun setExecutionData(executionData: ExecutionData) {
        executionDataStore = executionData
    }

    val buildData: BuildData
        get() = buildDataStore ?: throw NullPointerException("Build data must not be null")

    val executionData: ExecutionData
        get() = executionDataStore ?: throw NullPointerException("Execution data must not be null")
}