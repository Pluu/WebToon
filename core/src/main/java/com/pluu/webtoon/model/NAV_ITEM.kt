package com.pluu.webtoon.model

@Suppress("ClassName")
enum class NAV_ITEM {
    NAVER,
    DAUM,
    KTOON,
    KAKAOPAGE;

    companion object {
        fun getDefault(): NAV_ITEM = NAVER
    }
}
