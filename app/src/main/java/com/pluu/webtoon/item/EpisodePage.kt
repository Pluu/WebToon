package com.pluu.webtoon.item

import android.text.TextUtils
import com.pluu.support.impl.AbstractEpisodeApi

/**
 * Episode Page Info Class
 * Created by pluu on 2017-05-02.
 */
class EpisodePage(private val api: AbstractEpisodeApi) {
    var nextLink: String? = null
    var episodes: List<Episode>? = null
        get() {
            if (field != null) {
                return field
            }
            return emptyList()
        }

    fun moreLink(): String? {
        if (TextUtils.isEmpty(nextLink)) {
            return null
        }
        return api.moreParseEpisode(this)
    }
}
