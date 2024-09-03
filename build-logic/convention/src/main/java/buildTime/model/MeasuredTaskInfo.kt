package buildTime.model

import org.gradle.tooling.Failure
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

data class MeasuredTaskInfo(
    val isSuccessful: Boolean,
    val failures: List<Failure>?,
    val name: String,
    val startTime: Long,
    val finishedAt: Long,
    val path: String,
    val state: State,
) {
    val duration: Duration
        get() = (finishedAt - startTime).milliseconds

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