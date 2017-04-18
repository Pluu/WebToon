package com.pluu.webtoon.utils

import android.content.Context

import com.pluu.webtoon.R
import com.pluu.webtoon.item.ERROR_TYPE

/**
 * 메세지 유틸
 * Created by pluu on 2017-04-18.
 */
fun ERROR_TYPE.getMessage(context: Context) : String {
    val resId: Int
    when (this) {
        ERROR_TYPE.ADULT_CERTIFY, ERROR_TYPE.COINT_NEED -> resId = R.string.msg_not_support
        ERROR_TYPE.NOT_SUPPORT -> resId = R.string.not_support_type
        else -> resId = R.string.network_fail
    }
    return context.getString(resId)
}