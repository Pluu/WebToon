package buildTime.model

import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

data class MeasuredTask(
    val name: String,
    val startTime: Long,
    val endTime: Long,
    val state: State
) {
    val isCached: Boolean = state == State.IS_FROM_CACHE || state == State.UP_TO_DATE

    val duration: Duration = (endTime - startTime).milliseconds

    override fun toString(): String {
        return "[${startTime.toTime()}(${startTime}~${endTime.toTime()}(${endTime})] : $name"
    }

    private fun Long.toTime(): String {
        return LocalTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault()).toString()
    }

    enum class State {
        UP_TO_DATE,
        IS_FROM_CACHE,
        EXECUTED,
    }
}