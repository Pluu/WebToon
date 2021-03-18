package com.pluu.webtoon.model

import androidx.annotation.ColorRes
import com.pluu.webtoon.R

/**
 * Service Const Class
 * Created by pluu on 2015-11-01.
 */
object ServiceConst {
    // titles for nav drawer items (indices must correspond to the above)
    val NAV_DRAWER_TITLE_RES_ID = intArrayOf(
        R.string.title_naver,
        R.string.title_daum,
        R.string.title_olleh,
        R.string.title_kakao_page,
        R.string.title_nate
    )
}

@Suppress("ClassName")
enum class UI_NAV_ITEM(
    @ColorRes val color: Int = 0,
    @ColorRes val bgColor: Int = 0
) {
    NAVER(R.color.naver_color, R.color.naver_color_variant),
    DAUM(R.color.daum_color, R.color.daum_color_variant),
    KTOON(R.color.olleh_color, R.color.olleh_color_variant),
    KAKAOPAGE(R.color.kakao_color, R.color.kakao_color_variant),
    NATE(R.color.nate_color, R.color.nate_color_variant)
}

fun UI_NAV_ITEM.getCoreType(): NAV_ITEM = when (this) {
    UI_NAV_ITEM.NAVER -> NAV_ITEM.NAVER
    UI_NAV_ITEM.DAUM -> NAV_ITEM.DAUM
    UI_NAV_ITEM.KTOON -> NAV_ITEM.KTOON
    UI_NAV_ITEM.KAKAOPAGE -> NAV_ITEM.KAKAOPAGE
    UI_NAV_ITEM.NATE -> NAV_ITEM.NATE
}

fun NAV_ITEM.toUiType(): UI_NAV_ITEM = when (this) {
    NAV_ITEM.NAVER -> UI_NAV_ITEM.NAVER
    NAV_ITEM.DAUM -> UI_NAV_ITEM.DAUM
    NAV_ITEM.KTOON -> UI_NAV_ITEM.KTOON
    NAV_ITEM.KAKAOPAGE -> UI_NAV_ITEM.KAKAOPAGE
    NAV_ITEM.NATE -> UI_NAV_ITEM.NATE
}
