@file:Suppress("NOTHING_TO_INLINE")

package com.pluu.utils

import org.json.JSONArray
import org.json.JSONObject

inline fun JSONObject?.orEmpty(): JSONObject = this ?: JSONObject()

inline fun JSONArray?.orEmpty(): JSONArray = this ?: JSONArray()

inline fun <R> JSONObject.mapEach(transform: (String) -> R): List<R> {
    val result = ArrayList<R>(length())
    keys().forEach { key ->
        result.add(transform(getString(key)))
    }
    return result
}

/**
 * Custom Json Array Extends
 * Created by pluu on 2017-04-17.
 */
operator fun JSONArray.iterator(): Iterator<JSONObject> =
    (0 until length()).asSequence().map { get(it) as JSONObject }.iterator()

fun JSONArray.asSequence(): Sequence<JSONObject> {
    return object : Sequence<JSONObject> {
        override fun iterator() = object : Iterator<JSONObject> {
            val it = (0 until this@asSequence.length()).iterator()

            override fun next(): JSONObject {
                val i = it.next()
                return this@asSequence.get(i) as JSONObject
            }

            override fun hasNext() = it.hasNext()
        }
    }
}

fun JSONArray.isNotEmpty(): Boolean = length() > 0
