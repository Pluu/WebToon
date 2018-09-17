package com.pluu.webtoon.common

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

import com.pluu.support.impl.NAV_ITEM

/**
 * SharedPreferences
 * Created by pluu on 2017-04-29.
 */
object PrefConfig {

    const val KEY_DEFAULT_WEBTOON = "default_webtoon"

    /**
     * SharedPreferences 취득
     * @param context Context
     * @return SharedPreferences
     */
    private fun getPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getDefaultWebToon(context: Context): NAV_ITEM {
        val name = getPreferences(context).getString(
            KEY_DEFAULT_WEBTOON,
            NAV_ITEM.getDefault().name
        )
        return NAV_ITEM.valueOf(name!!.toUpperCase())
    }
}
