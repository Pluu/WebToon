package com.pluu.support.impl

import androidx.annotation.ColorRes
import com.pluu.webtoon.R

/**
 * Service Const Class
 * Created by PLUUSYSTEM-NEW on 2015-11-01.
 */
object ServiceConst {
    // titles for navdrawer items (indices must correspond to the above)
    val NAVDRAWER_TITLE_RES_ID = intArrayOf(
        R.string.title_naver,
        R.string.title_daum,
        R.string.title_olleh,
        R.string.title_kakao_page,
        R.string.title_nate
    )

    // icons for navdrawer items (indices must correspond to above array)
    val NAVDRAWER_ICON_RES_ID = IntArray(NAVDRAWER_TITLE_RES_ID.size) { 0 }
}

enum class NAV_ITEM(
    val isSelect: Boolean = false,
    @ColorRes val color: Int = 0,
    @ColorRes val bgColor: Int = 0
) {
    NAVER(true, R.color.naver_color, R.color.naver_color_dark),
    DAUM(true, R.color.daum_color, R.color.daum_color_dark),
    KTOON(true, R.color.olleh_color, R.color.olleh_color_dark),
    KAKAOPAGE(true, R.color.kakao_color, R.color.kakao_color_dark),
    NATE(true, R.color.nate_color, R.color.nate_color_dark),
    SEPARATOR(), // Separator
    INVALID();

    companion object {
        fun getDefault() = NAVER
    }
}
