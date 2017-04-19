package com.pluu.event

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * RxBus from RxJava
 * Created by pluu on 2017-04-19.
 */
class RxBus {
    private val mBus = PublishSubject.create<Any>()

    fun send(o: Any) {
        mBus.onNext(o)
    }

    fun toObservable(): Observable<Any> {
        return mBus
    }

    fun hasObservers(): Boolean {
        return mBus.hasObservers()
    }
}
