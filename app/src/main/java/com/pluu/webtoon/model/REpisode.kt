package com.pluu.webtoon.model

import io.realm.RealmObject

/**
 * 에피소드 Model
 * Created by pluu on 2017-05-02.
 */
open class REpisode : RealmObject() {
    open var service: String? = null
    open var toonId: String? = null
    open var episodeId: String? = null
}
