package com.pluu.webtoon.main.container.utils

import android.os.Bundle
import androidx.navigation.NavType
import com.pluu.webtoon.model.utils.parseNavigationValue
import com.pluu.webtoon.model.utils.toNavigationValue
import kotlinx.serialization.json.Json
import java.io.Serializable

inline fun <reified T : Serializable> navType(
    isNullableAllowed: Boolean = true,
    json: Json = Json,
): NavType<T> = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getSerializable(key, T::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putSerializable(key, value)
    }

    override fun parseValue(value: String): T {
        val value: T = value.parseNavigationValue()
        return value
    }

    override fun serializeAsValue(value: T): String {
        return value.toNavigationValue()
    }
}