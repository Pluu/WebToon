package com.pluu.webtoon.detail.model

import android.content.Context
import com.pluu.webtoon.detail.R
import com.pluu.webtoon.model.ERROR_TYPE

internal fun ERROR_TYPE.getMessage(context: Context): String {
    val resId: Int = when (this) {
        ERROR_TYPE.ADULT_CERTIFY, ERROR_TYPE.COIN_NEED -> com.pluu.webtoon.ui_common.R.string.msg_not_support
        ERROR_TYPE.NOT_SUPPORT -> R.string.not_support_type
        else -> com.pluu.webtoon.ui_common.R.string.network_fail
    }
    return context.getString(resId)
}
