package buildTime.extenstions

import kotlin.time.Duration

fun Duration.toTimeStamp(): String {
    val milliseconds = inWholeMilliseconds
    val minutes = milliseconds / 1000 / 60
    val seconds = milliseconds / 1000 % 60
    return "%dm %ds %dms".format(minutes, seconds, milliseconds % 1000)
}