package com.pluu.webtoon.domain.model

/**
 * 에러 타입
 * Created by pluu on 2017-04-18.
 */
@Suppress("ClassName")
sealed class ERROR_TYPE {
    // 성인 인증
    object ADULT_CERTIFY : ERROR_TYPE()

    // 코인 필요
    object COIN_NEED : ERROR_TYPE()

    // 지원 불가 웹툰 타입
    object NOT_SUPPORT : ERROR_TYPE()

    // 기본 에러
    class DEFAULT_ERROR(val throwable: Throwable? = null) : ERROR_TYPE()
}
