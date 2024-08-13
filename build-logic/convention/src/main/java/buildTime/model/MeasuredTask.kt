package buildTime.model

import kotlin.time.Duration

data class MeasuredTask(
    val name: String,
    val duration: Duration,
    val startTime: Long,
    val state: State
) {
    val isCached: Boolean = state == State.IS_FROM_CACHE || state == State.UP_TO_DATE

    enum class State {
        UP_TO_DATE,
        IS_FROM_CACHE,
        EXECUTED,
    }
}