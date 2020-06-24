package com.pluu.webtoon.weekly.ui.listener

import android.widget.ImageView
import com.pluu.webtoon.model.ToonInfo

/**
 * WebToon Item Select Listener
 * Created by pluu on 2017-05-02.
 */
internal interface WebToonSelectListener {

    /** 접근 불가능 아이템 선택 */
    fun selectLockItem()

    /** 접근 가능 아이템 선택 */
    fun selectSuccess(view: ImageView, item: ToonInfo)
}
