package com.pluu.kotlin

import org.json.JSONArray
import org.json.JSONObject

/**
 * Custom Extends
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