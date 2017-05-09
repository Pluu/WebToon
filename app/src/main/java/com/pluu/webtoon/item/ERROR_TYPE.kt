package com.pluu.webtoon.item

import android.content.Context
import com.pluu.webtoon.R

/**
 * 에러 타입
 * Created by pluu on 2017-04-18.
 */
enum class ERROR_TYPE {
    ADULT_CERTIFY, // 성인 인증
    COIN_NEED, // 코인 필요
    NOT_SUPPORT, // 지원 불가 웹툰 타입
    DEFAULT_ERROR
    // 기본 에러
}

fun ERROR_TYPE.getMessage(context: Context) : String {
    val resId: Int
    when (this) {
        ERROR_TYPE.ADULT_CERTIFY, ERROR_TYPE.COIN_NEED -> resId = R.string.msg_not_support
        ERROR_TYPE.NOT_SUPPORT -> resId = R.string.not_support_type
        else -> resId = R.string.network_fail
    }
    return context.getString(resId)
}