package com.pluu.event;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * RxBus from RxJava
 * Created by PLUUSYSTEM-NEW on 2016-03-21.
 */
public class RxBus {
    private final PublishSubject<Object> mBus = PublishSubject.create();

    public RxBus() { }

    public void send(Object o) {
        mBus.onNext(o);
    }

    public Observable<Object> toObservable() {
        return mBus;
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }
}
