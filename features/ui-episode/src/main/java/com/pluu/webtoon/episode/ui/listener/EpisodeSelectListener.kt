package com.pluu.webtoon.episode.ui.listener

import com.pluu.webtoon.model.EpisodeInfo

/**
 * EpisodeInfo Item Select Listener
 * Created by pluu on 2017-05-02.
 */
internal interface EpisodeSelectListener {

    /**
     * 접근 불가능 아이템 선택
     */
    fun selectLockItem()

    /**
     * 접근 가능 아이템 선택
     */
    fun selectSuccess(item: EpisodeInfo)
}
