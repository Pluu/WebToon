package com.pluu.support.impl

import com.pluu.webtoon.di.NetworkModule
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.EpisodePage
import com.pluu.webtoon.item.WebToonInfo

/**
 * Episode API
 * Created by pluu on 2017-04-20.
 */
abstract class AbstractEpisodeApi(
    networkModule: NetworkModule
) : NetworkSupportApi(networkModule) {

    open fun init() {}

    abstract fun parseEpisode(info: WebToonInfo): EpisodePage

    abstract fun moreParseEpisode(item: EpisodePage): String?

    abstract fun getFirstEpisode(item: Episode): Episode?
}
