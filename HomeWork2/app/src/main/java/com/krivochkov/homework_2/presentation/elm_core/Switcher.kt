package com.krivochkov.homework_2.presentation.elm_core

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class Switcher {

    private val nextRequestId = AtomicInteger(0)
    private val requests = PublishSubject.create<Int>()

    fun <Event : Any> observable(
        delayMillis: Long = 0,
        action: () -> Observable<Event>,
    ): Observable<Event> {
        val requestId = nextRequestId.getAndIncrement()
        requests.onNext(requestId)
        return Completable.timer(delayMillis, TimeUnit.MILLISECONDS)
            .andThen(action())
            .takeUntil(requests.filter { it > requestId })
    }
}