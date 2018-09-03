package com.pluu.support.impl

import com.pluu.webtoon.di.NetworkModule
import com.pluu.webtoon.item.Detail
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.ShareItem

/**
 * Detail Parse API
 * Created by pluu on 2017-04-20.
 */
abstract class AbstractDetailApi(
    networkModule: NetworkModule
) : NetworkSupportApi(networkModule) {

    abstract fun parseDetail(episode: Episode): Detail

    abstract fun getDetailShare(episode: Episode, detail: Detail): ShareItem
}
