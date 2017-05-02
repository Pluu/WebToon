package com.pluu.webtoon.ui.listener

import com.pluu.webtoon.item.Episode

/**
 * Episode Item Select Listener
 * Created by pluu on 2017-05-02.
 */
interface EpisodeSelectListener {

    /**
     * 접근 불가능 아이템 선택
     */
    fun selectLockItem()

    /**
     * 접근 가능 아이템 선택
     */
    fun selectSuccess(item: Episode)

}