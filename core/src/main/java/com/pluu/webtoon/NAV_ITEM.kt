package com.pluu.webtoon

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
