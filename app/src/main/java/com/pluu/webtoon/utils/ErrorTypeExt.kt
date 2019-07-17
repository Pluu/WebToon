package com.pluu.webtoon.utils

import android.content.Context
import com.pluu.webtoon.R
import com.pluu.webtoon.domain.moel.ERROR_TYPE

fun ERROR_TYPE.getMessage(context: Context): String {
    val resId: Int = when (this) {
        ERROR_TYPE.ADULT_CERTIFY, ERROR_TYPE.COIN_NEED -> R.string.msg_not_support
        ERROR_TYPE.NOT_SUPPORT -> R.string.not_support_type
        else -> R.string.network_fail
    }
    return context.getString(resId)
}
