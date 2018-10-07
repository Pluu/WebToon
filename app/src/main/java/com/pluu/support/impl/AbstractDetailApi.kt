package com.pluu.support.impl

import com.pluu.webtoon.di.NetworkUseCase
import com.pluu.webtoon.item.DetailResult
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.IEpisode
import com.pluu.webtoon.item.ShareItem

/**
 * Detail Parse API
 * Created by pluu on 2017-04-20.
 */
abstract class AbstractDetailApi(
    networkUseCase: NetworkUseCase
) : NetworkSupportApi(networkUseCase) {

    abstract fun parseDetail(episode: IEpisode): DetailResult

    abstract fun getDetailShare(episode: Episode, detail: DetailResult.Detail): ShareItem
}
