package com.pluu.utils

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable

inline fun <reified T : Parcelable> Intent.getRequiredParcelableExtra(name: String): T =
    getParcelableExtra(name) ?: throw IllegalArgumentException("$name does not reference")

inline fun <reified T : Parcelable> Bundle.getRequiredParcelableExtra(name: String): T =
    getParcelable(name) ?: throw IllegalArgumentException("$name does not reference")
