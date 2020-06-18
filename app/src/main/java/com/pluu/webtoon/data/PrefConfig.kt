package com.pluu.webtoon.data

import android.content.Context
import androidx.preference.PreferenceManager
import com.pluu.webtoon.NAV_ITEM
import java.util.Locale

/**
 * SharedPreferences
 * Created by pluu on 2017-04-29.
 */
class PrefConfig(
    context: Context
) {
    private val pref = PreferenceManager.getDefaultSharedPreferences(context)

    fun getDefaultWebToon(): NAV_ITEM {
        val name = pref.getString(KEY_DEFAULT_WEBTOON, null) ?: NAV_ITEM.getDefault().name
        return NAV_ITEM.valueOf(name.toUpperCase(Locale.ROOT))
    }

    companion object {
        const val KEY_DEFAULT_WEBTOON = "default_webtoon"
    }
}
