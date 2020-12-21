package android.ru.romashkaapp.usecases

import android.annotation.SuppressLint
import android.ru.romashkaapp.data.net.repository.ApiRepository
import android.ru.romashkaapp.models.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by yasina on 01.10.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
@SuppressLint("CheckResult")
class DictionaryUseCase(
    private val mRepository: ApiRepository,
    private val mAccessToken: String
): ApiUseCase () {

    fun <S> getNoms(perPage: Int?, useCaseDisposable: S) where S : Observer<in MutableList<NomModel>>?, S : Disposable {
        getNoms(perPage, null, useCaseDisposable)
    }

    fun <S> getNoms(perPage: Int?, page: Int?, useCaseDisposable: S) where S : Observer<in MutableList<NomModel>>?, S : Disposable {
        mRepository.getNoms(mAccessToken, perPage, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getHall(id: Int, useCaseDisposable: S) where S : Observer<in HallModel>?, S : Disposable {
        mRepository.getHall(mAccessToken, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

//    fun <S> getServices(last: String?,
//                        limit: String?,
//                        active: Boolean?,
//                        unitId: Int?, useCaseDisposable: S) where S : Observer<in MutableList<ServiceModel>>?, S : Disposable {
//        mRepository.getServices(last, limit, active, unitId)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeWith(useCaseDisposable)
//    }
//
//    fun <S> getService(serviceId: Int, useCaseDisposable: S) where S : Observer<in ServiceModel>?, S : Disposable {
//        mRepository.getService(serviceId)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeWith(useCaseDisposable)
//    }
}