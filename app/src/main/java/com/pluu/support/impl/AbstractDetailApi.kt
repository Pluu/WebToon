package com.pluu.support.impl

import com.pluu.webtoon.di.NetworkUseCase
import com.pluu.webtoon.item.Detail
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.ShareItem

/**
 * Detail Parse API
 * Created by pluu on 2017-04-20.
 */
abstract class AbstractDetailApi(
    networkUseCase: NetworkUseCase
) : NetworkSupportApi(networkUseCase) {

    abstract fun parseDetail(episode: Episode): Detail

    abstract fun getDetailShare(episode: Episode, detail: Detail): ShareItem
}
