package com.pluu.webtoon.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations

inline fun LiveData<Unit>.observeNonNull(
    owner: LifecycleOwner,
    crossinline observer: () -> Unit
) {
    this.observe(owner, Observer {
        if (it != null) {
            observer()
        }
    })
}

inline fun <T> LiveData<T>.observe(
    owner: LifecycleOwner,
    crossinline observer: (T?) -> Unit
) {
    observe(owner, Observer<T> { v -> observer(v) })
}

inline fun <T> LiveData<T>.observeNonNull(
    owner: LifecycleOwner,
    crossinline observer: (T) -> Unit
) {
    this.observe(owner, Observer {
        if (it != null) {
            observer(it)
        }
    })
}

inline fun <X, Y> LiveData<X>.map(
    crossinline transformer: (X) -> Y
): LiveData<Y> = Transformations.map(this) { transformer(it) }

inline fun <X, Y> LiveData<X>.switchMap(
    crossinline transformer: (X) -> LiveData<Y>
): LiveData<Y> = Transformations.switchMap(this) { transformer(it) }
