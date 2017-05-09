package com.pluu.support.impl

import android.os.Bundle
import android.support.annotation.ColorRes

import com.pluu.webtoon.R
import com.pluu.webtoon.common.Const

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
            R.string.title_nate,
            R.string.title_t_store)

    // icons for navdrawer items (indices must correspond to above array)
    val NAVDRAWER_ICON_RES_ID = intArrayOf(
            0, // NAVER
            0, // DAUM
            0, // OLLEH
            0, // Kakao Page
            0, // Nate
            0) // ONE Store

    fun getApiType(bundle: Bundle?): NAV_ITEM = if (bundle != null)
        bundle.getSerializable(Const.EXTRA_API) as NAV_ITEM
    else
        NAV_ITEM.getDefault()
}

enum class NAV_ITEM (val isSelect: Boolean = false, @param:ColorRes val color: Int = 0, @param:ColorRes val bgColor: Int = 0) {
    NAVER(true, R.color.naver_color, R.color.naver_color_dark),
    DAUM(true, R.color.daum_color, R.color.daum_color_dark),
    OLLEH(true, R.color.olleh_color, R.color.olleh_color_dark),
    KAKAOPAGE(true, R.color.kakao_color, R.color.kakao_color_dark),
    NATE(true, R.color.nate_color, R.color.nate_color_dark),
    T_STORE(true, R.color.t_store_color, R.color.t_store_color_dark),
    SEPARATOR(), // Separator
    INVALID();

    companion object {
        fun getDefault() = NAVER
    }
}