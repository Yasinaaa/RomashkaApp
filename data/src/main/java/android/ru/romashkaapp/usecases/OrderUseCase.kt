package android.ru.romashkaapp.usecases

import android.annotation.SuppressLint
import android.ru.romashkaapp.data.net.repository.ApiRepository
import android.ru.romashkaapp.models.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

/**
 * Created by yasina on 01.10.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
@SuppressLint("CheckResult")
class OrderUseCase(
    private val mRepository: ApiRepository,
    private val mAccessToken: String?
): ApiUseCase () {

    fun<T> get(ob: Observable<T>, useCaseDisposable: Observer<in T>){
        ob.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> addToCart(eventId: Int,
                      areaId: Int,
                      sid: String, useCaseDisposable: S) where S : Observer<in OrderIdModel>?, S : Disposable {
        if (mAccessToken != null) {
            mRepository.addToCart(eventId, areaId, sid, mAccessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(useCaseDisposable)
        }
    }

    fun <S> deleteOrder(eventId: Int,
                         areaId: Int,
                         sid: String, useCaseDisposable: S) where S : Observer<in ResponseBody>?, S : Disposable {
        if (mAccessToken != null) {
            mRepository.deleteSeatFromCart(eventId, areaId, sid, mAccessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(useCaseDisposable)
        }
    }

    fun <S> payOrder(orderId: Int, useCaseDisposable: S) where S : Observer<in ResponseBody>?, S : Disposable {
        if (mAccessToken != null) {
            mRepository.payOrder(orderId, mAccessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(useCaseDisposable)
        }
    }

    fun <S> getAllOrders(useCaseDisposable: S) where S : Observer<in MutableList<OrderModel>>?, S : Disposable {
        mRepository.getAllOrders(mAccessToken!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getOrder(orderId:Int, useCaseDisposable: S) where S : Observer<in OrderModel>?, S : Disposable {
        mRepository.getOrder(orderId, mAccessToken!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getUserOrderCarts(orderId:Int, useCaseDisposable: S) where S : Observer<in MutableList<CartModel>>?, S : Disposable {
        mRepository.getUserOrderCarts(orderId, mAccessToken!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getTickets(orderId: Int, useCaseDisposable: S) where S : Observer<in ResponseBody>?, S : Disposable {
        if (mAccessToken != null) {
            mRepository.getTickets(orderId, mAccessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(useCaseDisposable)
        }
    }
}