package buildTime.model

import org.gradle.tooling.Failure
import kotlin.time.Duration

data class MeasuredTaskInfo(
    val isSuccessful: Boolean,
    val failures: List<Failure>?,
    val name: String,
    val duration: Duration,
    val startTime: Long,
    val state: State,
) {
    val isCached: Boolean = when (state) {
        State.IS_FROM_CACHE,
        State.UP_TO_DATE,
        State.INCREMENTAL -> true

        else -> false
    }

    enum class State {
        UP_TO_DATE,
        IS_FROM_CACHE,
        EXECUTED,
        INCREMENTAL,
    }
}