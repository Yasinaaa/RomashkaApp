package android.ru.romashkaapp.usecases

import android.annotation.SuppressLint
import android.ru.romashkaapp.data.net.repository.ApiRepository
import android.ru.romashkaapp.models.*
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

    fun <S> getCategories(accessToken: String, last: String?, limit: String?, useCaseDisposable: S) where S : Observer<in MutableList<CategoryModel>>?, S : Disposable {
        mRepository.getCategories(accessToken, last, limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getCategory(categoryId: Int, useCaseDisposable: S) where S : Observer<in CategoryModel>?, S : Disposable {
        mRepository.getCategory(categoryId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getActions(accessToken: String, useCaseDisposable: S) where S : Observer<in MutableList<ActionModel>>?, S : Disposable {
        mRepository.getActions(accessToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getAction(actionId: Int, useCaseDisposable: S) where S : Observer<in ActionModel>?, S : Disposable {
        mRepository.getAction(actionId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getNoms(last: String?, limit: String?, useCaseDisposable: S) where S : Observer<in MutableList<NomModel>>?, S : Disposable {
        mRepository.getNoms(mAccessToken, last, limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getNom(id: Int, useCaseDisposable: S) where S : Observer<in NomModel>?, S : Disposable {
        mRepository.getNom(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getCities(last: String?, limit: String?, useCaseDisposable: S) where S : Observer<in MutableList<CityModel>>?, S : Disposable {
        mRepository.getCities(last, limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getCity(id: Int, useCaseDisposable: S) where S : Observer<in CityModel>?, S : Disposable {
        mRepository.getCity(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getUnits(last: String?, limit: String?, useCaseDisposable: S) where S : Observer<in MutableList<UnitModel>>?, S : Disposable {
        mRepository.getUnits(last, limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getUnit(id: Int, useCaseDisposable: S) where S : Observer<in UnitModel>?, S : Disposable {
        mRepository.getUnit(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getHalls(last: String?, limit: String?, useCaseDisposable: S) where S : Observer<in MutableList<HallModel>>?, S : Disposable {
        mRepository.getHalls(last, limit)
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

    fun <S> getServices(last: String?,
                        limit: String?,
                        active: Boolean?,
                        unitId: Int?, useCaseDisposable: S) where S : Observer<in MutableList<ServiceModel>>?, S : Disposable {
        mRepository.getServices(last, limit, active, unitId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getService(serviceId: Int, useCaseDisposable: S) where S : Observer<in ServiceModel>?, S : Disposable {
        mRepository.getService(serviceId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }
}