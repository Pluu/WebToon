package com.pluu.event

/**
 * RxBusProvider from RxJava
 * Created by pluu on 2017-04-19.
 */
class RxBusProvider private constructor() {

    companion object {
        private val _instance = RxBus()

        fun getInstance() = _instance

    }
}
