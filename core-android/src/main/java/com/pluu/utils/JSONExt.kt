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