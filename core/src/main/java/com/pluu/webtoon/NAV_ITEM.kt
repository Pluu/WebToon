package com.pluu.webtoon

@Suppress("ClassName")
enum class NAV_ITEM {
    NAVER,
    DAUM,
    KTOON,
    KAKAOPAGE,
    NATE;

    companion object {
        fun getDefault() = NAVER
    }
}
