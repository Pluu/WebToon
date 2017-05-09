package com.pluu.support.impl

import android.content.Context
import android.content.res.Resources
import android.support.v4.content.ContextCompat
import com.pluu.support.daum.DaumWeekApi
import com.pluu.support.kakao.KakaoWeekApi
import com.pluu.support.nate.NateWeekApi
import com.pluu.support.naver.NaverWeekApi
import com.pluu.support.olleh.OllehWeekApi
import com.pluu.support.tstore.TStorerWeekApi
import com.pluu.webtoon.item.WebToonInfo
import java.util.*

/**
 * Week API
 * Created by pluu on 2017-04-20.
 */
abstract class AbstractWeekApi
    protected constructor(context: Context, private val CURRENT_TABS: Array<String>) : NetworkSupportApi(context) {

    abstract val naviItem: NAV_ITEM

    fun getTitleColor(context: Context): Int {
        return ContextCompat.getColor(context, naviItem.color)
    }

    fun getTitleColorDark(context: Context): Int {
        return ContextCompat.getColor(context, naviItem.bgColor)
    }

    val weeklyTabSize: Int
        get() = CURRENT_TABS.size

    fun getWeeklyTabName(position: Int): String {
        return CURRENT_TABS[position]
    }

    val todayTabPosition: Int
        get() = (Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK) + 5) % 7

    @Throws(Exception::class)
    abstract fun parseMain(position: Int): List<WebToonInfo>

    companion object {

        fun getApi(context: Context, item: NAV_ITEM): AbstractWeekApi {
            when (item) {
                NAV_ITEM.NAVER -> return NaverWeekApi(context)
                NAV_ITEM.DAUM -> return DaumWeekApi(context)
                NAV_ITEM.OLLEH -> return OllehWeekApi(context)
                NAV_ITEM.KAKAOPAGE -> return KakaoWeekApi(context)
                NAV_ITEM.NATE -> return NateWeekApi(context)
                NAV_ITEM.T_STORE -> return TStorerWeekApi(context)
                else -> throw Resources.NotFoundException("Not Found API")
            }
        }
    }

}
