package com.pluu.kotlin

import org.json.JSONArray
import org.json.JSONObject

/**
 * Custom Extends
 * Created by pluu on 2017-04-17.
 */
operator fun JSONArray.iterator(): Iterator<JSONObject>
        = (0 until length()).asSequence().map { get(it) as JSONObject }.iterator()

fun JSONArray.isNotEmpty(): Boolean = length() > 0