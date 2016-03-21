package com.pluu.event;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * RxBus from RxJava
 * Created by PLUUSYSTEM-NEW on 2016-03-21.
 */
public class RxBus {
    private final Subject<Object, Object> mBus =
            new SerializedSubject<>(PublishSubject.create());

    public RxBus() {
    }

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
