@file:Suppress("NOTHING_TO_INLINE")

package com.pluu.core

import org.json.JSONArray
import org.json.JSONObject

inline fun JSONObject?.orEmpty(): JSONObject = this ?: JSONObject()

inline fun JSONArray?.orEmpty(): JSONArray = this ?: JSONArray()
