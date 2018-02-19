package com.pluu.kotlin

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * String to Date Convert Extension Function
 * @param pattern date and time format
 * @return Date or null
 */
fun String.toDate(
    pattern: String,
    locale: Locale = Locale.US
): Date? {
    val sdFormat = try {
        SimpleDateFormat(pattern, locale)
    } catch (e: IllegalArgumentException) {
        null
    }
    return sdFormat?.let {
        try {
            it.parse(this)
        } catch (e: ParseException) {
            null
        }
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