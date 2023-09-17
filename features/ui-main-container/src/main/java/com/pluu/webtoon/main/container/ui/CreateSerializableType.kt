package com.pluu.webtoon.main.container.ui

import androidx.navigation.NavType

class SerializableType<T : java.io.Serializable>(
    type: Class<T>,
    private val parser: (String) -> T
) : NavType.SerializableType<T>(type) {
    override fun parseValue(value: String): T {
        return parser(value)
    }
}