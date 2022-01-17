package com.pluu.webtoon.data.local.pref

import android.content.SharedPreferences
import com.pluu.webtoon.model.KeyContract
import com.pluu.webtoon.model.NAV_ITEM
import java.util.Locale
import javax.inject.Inject

/**
 * SharedPreferences
 * Created by pluu on 2017-04-29.
 */
class PrefConfig @Inject constructor(
    private val pref: SharedPreferences
) {
    fun getDefaultWebToon(): NAV_ITEM {
        val name = pref.getString(KeyContract.KEY_DEFAULT_WEBTOON, null) ?: NAV_ITEM.getDefault().name
        return NAV_ITEM.valueOf(name.uppercase(Locale.ROOT))
    }
}
