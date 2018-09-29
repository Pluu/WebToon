package com.pluu.support.impl

import com.pluu.webtoon.di.NetworkUseCase
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.EpisodePage
import com.pluu.webtoon.item.WebToonInfo

/**
 * Episode API
 * Created by pluu on 2017-04-20.
 */
abstract class AbstractEpisodeApi(
    networkUseCase: NetworkUseCase
) : NetworkSupportApi(networkUseCase) {

    open fun init() {}

    abstract fun parseEpisode(info: WebToonInfo): EpisodePage

    abstract fun moreParseEpisode(item: EpisodePage): String?

    abstract fun getFirstEpisode(item: Episode): Episode?
}
