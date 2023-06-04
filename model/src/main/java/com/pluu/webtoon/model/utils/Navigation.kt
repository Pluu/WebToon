package com.pluu.webtoon.model.utils

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder

inline fun <reified T> T.toNavigationValue(): String =
    URLEncoder.encode(Json.encodeToString(this), "UTF-8")

inline fun <reified T> String.parseNavigationValue(): T =
    Json.decodeFromString(this)