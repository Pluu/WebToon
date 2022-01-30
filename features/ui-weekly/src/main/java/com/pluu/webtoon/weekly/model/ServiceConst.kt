package com.pluu.webtoon.weekly.model

import androidx.compose.ui.graphics.Color
import com.pluu.webtoon.model.NAV_ITEM

/**
 * Service Const Class
 * Created by pluu on 2015-11-01.
 */
object ServiceConst {
    // titles for nav drawer items (indices must correspond to the above)
    val NAV_DRAWER_TITLE_RES_ID = intArrayOf(
        com.pluu.webtoon.ui_common.R.string.title_naver,
        com.pluu.webtoon.ui_common.R.string.title_daum,
        com.pluu.webtoon.ui_common.R.string.title_olleh,
        com.pluu.webtoon.ui_common.R.string.title_kakao_page
    )
}

@Suppress("ClassName")
enum class UI_NAV_ITEM(
    val color: Color,
    val bgColor: Color
) {
    NAVER(Color(0xFF2D8400), Color(0xFF246900)),
    DAUM(Color(0xFF608EFC), Color(0xFF4C71C9)),
    KTOON(Color(0xFFDF2E1C), Color(0xFFB22416)),
    KAKAOPAGE(Color(0xFF3F3035), Color(0xFF32262A))
}

fun UI_NAV_ITEM.getCoreType(): NAV_ITEM = when (this) {
    UI_NAV_ITEM.NAVER -> NAV_ITEM.NAVER
    UI_NAV_ITEM.DAUM -> NAV_ITEM.DAUM
    UI_NAV_ITEM.KTOON -> NAV_ITEM.KTOON
    UI_NAV_ITEM.KAKAOPAGE -> NAV_ITEM.KAKAOPAGE
}

fun NAV_ITEM.toUiType(): UI_NAV_ITEM = when (this) {
    NAV_ITEM.NAVER -> UI_NAV_ITEM.NAVER
    NAV_ITEM.DAUM -> UI_NAV_ITEM.DAUM
    NAV_ITEM.KTOON -> UI_NAV_ITEM.KTOON
    NAV_ITEM.KAKAOPAGE -> UI_NAV_ITEM.KAKAOPAGE
}
