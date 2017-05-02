package com.pluu.webtoon.model

import io.realm.RealmObject

/**
 * 웹툰 Model
 * Created by pluu on 2017-05-02.
 */
open class RToon : RealmObject() {
    open var service: String? = null
    open var toonId: String? = null
}
