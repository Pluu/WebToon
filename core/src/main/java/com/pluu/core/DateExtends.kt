package com.pluu.core

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * String to Date Convert Extension Function
 * @param pattern date and time format
 * @return Date or null
 */
fun String.toDate(
    pattern: String,
    locale: Locale = Locale.US
): Date? {
    val sdFormat = runCatching {
        SimpleDateFormat(pattern, locale)
    }.getOrNull()

    return sdFormat?.let {
        runCatching {
            it.parse(this)
        }.getOrNull()
    }
}

/**
 * Date to SimpleDateFormat String
 * @param pattern date and time format
 * @return Formatted String or Original String
 */
fun Date.toFormatString(
    pattern: String,
    locale: Locale = Locale.US
): String = try {
    SimpleDateFormat(pattern, locale).format(this)
} catch (e: IllegalArgumentException) {
    toString()
}
