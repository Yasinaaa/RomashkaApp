package android.ru.romashkaapp.usecases

import android.annotation.SuppressLint
import android.content.Context
import android.ru.romashkaapp.data.net.repository.ApiRepository
import android.ru.romashkaapp.models.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by yasina on 01.10.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
@SuppressLint("CheckResult")
class EventsUseCase(
    private val mRepository: ApiRepository,
    private val mAccessToken: String
): ApiUseCase () {

    fun<T> get(ob: Observable<T>, useCaseDisposable: Observer<in T>){
        ob.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getEvents(page: Int?, perPage: Int?, useCaseDisposable: S) where S : Observer<in MutableList<EventModel>>?, S : Disposable {
        mRepository.getEvents(mAccessToken, page, perPage)
            .subscribeOn(Schedulers.io())
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
        mRepository.getEvent(mAccessToken, eventId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getEventAreas(eventId: Int, useCaseDisposable: S) where S : Observer<in MutableList<AreaModel>>?, S : Disposable {
        mRepository.getEventAreas(eventId, mAccessToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getEventArea(eventId: Int, areaId: Int, useCaseDisposable: S) where S : Observer<in MutableList<SectorModel>>?, S : Disposable {
        mRepository.getEventArea(eventId, areaId, mAccessToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getEventAreaPlan(eventId: Int, areaId: Int, useCaseDisposable: S) where S : Observer<ResponseBody>?, S : Disposable {
        mRepository.getEventAreaPlan(eventId, areaId, mAccessToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getEventSectorSeats(eventId: Int,
                                sectorId: Int,
                                areaId: Int,
                                type: String?, useCaseDisposable: S) where S : Observer<MutableList<SeatModel>>?, S : Disposable {
        mRepository.getEventSectorSeats(eventId, sectorId, areaId, type, mAccessToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getEventSectorZones(eventId: Int,
                                areaId: Int, useCaseDisposable: S) where S : Observer<MutableList<ZoneModel>>?, S : Disposable {
        mRepository.getEventSectorZones(eventId, areaId, mAccessToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getEventSectorStatuses(eventId: Int,
                                   sectorId: Int,
                                   areaId: Int, useCaseDisposable: S) where S : Observer<MutableList<StatusModel>>?, S : Disposable {
        mRepository.getEventSectorStatuses(eventId, sectorId, areaId, mAccessToken)
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




    fun <S> getEventSectorPoints(eventId: Int,
                                sectorId: Int,
                                limit: Int, useCaseDisposable: S) where S : Observer<MutableList<PointModel>>?, S : Disposable {
        mRepository.getEventSectorPoints(eventId, sectorId, limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

}