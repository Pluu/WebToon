package com.pluu.webtoon.data.remote.utils

import android.content.res.Resources
import androidx.annotation.RawRes

internal class ResourceLoader(
    private val resource: Resources
) {
    fun readRawResource(@RawRes rawResId: Int): String {
        return resource.openRawResource(rawResId)
            .bufferedReader()
            .lineSequence()
            .joinToString(separator = "\n")
    }
}