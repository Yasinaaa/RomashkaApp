package ru.android.romashkaapp

import io.reactivex.observers.DisposableObserver

/**
 * Created by yasina on 05/09/2019
 */

open class BaseSubscriber<T>(): DisposableObserver<T>() {


    override fun onComplete() {}

    override fun onError(e: Throwable) {

    }

    override fun onNext(response: T) {
    }

}