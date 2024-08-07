package com.pluu.webtoon.main.container.ui

import androidx.navigation.NavType
import java.io.Serializable

class SerializableType<T : Serializable>(
    type: Class<T>,
    private val parser: (String) -> T
) : NavType.SerializableType<T>(type) {
    override fun parseValue(value: String): T {
        return parser(value)
    }
}