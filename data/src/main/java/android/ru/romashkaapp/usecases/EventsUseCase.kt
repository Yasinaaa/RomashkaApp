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
class EventsUseCase(
    private val mRepository: ApiRepository
): ApiUseCase () {

    fun<T> get(ob: Observable<T>, useCaseDisposable: Observer<in T>){
        ob.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getEvents(last: String?, limit: String?,
                      active: Boolean?, unitId: Int?,
                      hallId: Int?, nomId: Int?,
                      actionId: Int?, categoryId: Int?,
                      sdateGt: String?, sdateLs: String?,
                      edateGt: String?, edateLs: String?,
                      type: String?, useCaseDisposable: S) where S : Observer<in MutableList<EventModel>>?, S : Disposable {
        mRepository.getEvents(last, limit, active, unitId, hallId, nomId, actionId, categoryId, sdateGt, sdateLs, edateGt, edateLs, type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getEvent(eventId: Int, useCaseDisposable: S) where S : Observer<EventModel>?, S : Disposable {
        mRepository.getEvent(eventId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getEventSubscriptions(eventId: Int, useCaseDisposable: S) where S : Observer<MutableList<EventModel>>?, S : Disposable {
        mRepository.getEventSubscriptions(eventId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getEventSector(eventId: Int,
                           sectorId: Int,
                           last: String?,
                           lastSeatsGt: String?,
                           lastAreaGt: String?, useCaseDisposable: S) where S : Observer<SectorModel>?, S : Disposable {
        mRepository.getEventSector(eventId, sectorId, last, lastSeatsGt, lastAreaGt)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getEventSectorZones(eventId: Int,
                                sectorId: Int,
                                limit: Int, useCaseDisposable: S) where S : Observer<MutableList<ZoneModel>>?, S : Disposable {
        mRepository.getEventSectorZones(eventId, sectorId, limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getEventSectorSeats(eventId: Int,
                                sectorId: Int,
                                limit: Int, useCaseDisposable: S) where S : Observer<MutableList<SeatModel>>?, S : Disposable {
        mRepository.getEventSectorSeats(eventId, sectorId, limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getEventSectorPoints(eventId: Int,
                                sectorId: Int,
                                limit: Int, useCaseDisposable: S) where S : Observer<MutableList<PointModel>>?, S : Disposable {
        mRepository.getEventSectorPoints(eventId, sectorId, limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getEventSectorImage(eventId: Int,
                                 sectorId: Int, useCaseDisposable: S) where S : Observer<SectorImageModel>?, S : Disposable {
        mRepository.getEventSectorImage(eventId, sectorId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getEventSectorSvg(eventId: Int,
                              sectorId: Int, useCaseDisposable: S) where S : Observer<SectorSvgModel>?, S : Disposable {
        mRepository.getEventSectorSvg(eventId, sectorId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }
}