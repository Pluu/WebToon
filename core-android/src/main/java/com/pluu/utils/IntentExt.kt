package com.pluu.utils

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

inline fun <reified T : Serializable> Intent.getRequiredSerializable(
    name: String
): T = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    requireNotNull(getSerializableExtra(name, T::class.java))
} else {
    getSerializableExtra(name) as T
}

inline fun <reified T : Parcelable> Intent.getRequiredParcelableExtra(
    name: String
): T = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    requireNotNull(getParcelableExtra(name, T::class.java))
} else {
    requireNotNull(getParcelableExtra(name)) as T
}

inline fun <reified T : Serializable> Bundle.getRequiredSerializableExtra(
    name: String
): T = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    requireNotNull(getSerializable(name, T::class.java))
} else {
    getSerializable(name) as T
}

inline fun <reified T : Parcelable> Bundle.getRequiredParcelableExtra(
    name: String
): T = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    requireNotNull(getParcelable(name, T::class.java))
} else {
    requireNotNull(getParcelable(name)) as T
}