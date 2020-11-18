package ru.android.romashkaapp.afisha

import android.app.Application
import android.content.Context
import android.ru.romashkaapp.models.*
import android.ru.romashkaapp.usecases.EventsUseCase
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import ru.android.romashkaapp.BaseSubscriber
import ru.android.romashkaapp.StartActivity
import ru.android.romashkaapp.base.BaseViewModel
import ru.android.romashkaapp.matches.ItemClickListener
import ru.android.romashkaapp.utils.Utils.Companion.getAccessToken

/**
 * Created by yasina on 02.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class AfishaViewModel(application: Application) : BaseViewModel(application), View.OnClickListener,
    ItemClickListener {

    private var eventUseCase: EventsUseCase? = null
    val matchesList: MutableLiveData<MutableList<EventModel>> = MutableLiveData()
    val servicesList: MutableLiveData<MutableList<EventModel>> = MutableLiveData()
    val viewAllClick: MutableLiveData<Boolean> = MutableLiveData()
    val nextFragmentOpenClick = MutableLiveData<EventModel?>()

    init {
        matchesList.value = arrayListOf(EventModel(), EventModel(), EventModel())
        servicesList.value = arrayListOf(EventModel())

        eventUseCase = EventsUseCase(StartActivity.REPOSITORY)
    }

    fun getEvents(cont: Context){
        eventUseCase!!.getEvents(accessToken = getAccessToken(cont)!!, page = null, perPage = null, EventsSubscriber())
        //, page = null, perPage = null, EventsSubscriber())
    }

    private inner class EventsSubscriber: BaseSubscriber<MutableList<EventModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<EventModel>) {
            super.onNext(response)
            Log.d("ffd", "EventsSubscriber")

//            eventUseCase!!.getEvent(
//                response[0].id,
//                EventSubscriber())
        }
    }

    override fun onClick(view: View?) {

    }

    fun onViewAllBtnClick(){
        viewAllClick.value = true
    }

    override fun click(item: EventModel?) {
        nextFragmentOpenClick.value = item
    }

    fun getListener(): ItemClickListener{
        return this
    }

    private inner class EventSubscriber: BaseSubscriber<EventModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: EventModel) {
            super.onNext(response)
            Log.d("ffd", "EventSubscriber")

            eventUseCase!!.getEventSubscriptions(
                response.id,
                EventSubscriptionsSubscriber())
        }
    }

    private inner class EventSubscriptionsSubscriber: BaseSubscriber<MutableList<EventModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<EventModel>) {
            super.onNext(response)
            Log.d("ffd", "EventSubscriptionsSubscriber")

            eventUseCase!!.getEventSector(
                eventId = response[0].id,
                sectorId = 1,
                last= "100",
                lastSeatsGt = "100",
                lastAreaGt = "100",
                EventSectorZonesSubscriber())
        }
    }

    private inner class EventSectorZonesSubscriber: BaseSubscriber<SectorModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: SectorModel) {
            super.onNext(response)
            Log.d("ffd", "EventSectorZonesSubscriber")

            eventUseCase!!.getEventSectorSeats(
                eventId = response.id,
                sectorId = 1,
                limit = 100,
                EventSectorSeatsSubscriber())
        }
    }

    private inner class EventSectorSeatsSubscriber: BaseSubscriber<MutableList<SeatModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<SeatModel>) {
            super.onNext(response)
            Log.d("ffd", "EventSectorSeatsSubscriber")

            eventUseCase!!.getEventSectorPoints(
                eventId = 12,
                sectorId = 1,
                limit = 100,
                EventSectorPointsSubscriber())
        }
    }

    private inner class EventSectorPointsSubscriber: BaseSubscriber<MutableList<PointModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<PointModel>) {
            super.onNext(response)
            Log.d("ffd", "EventSectorPointsSubscriber")

            eventUseCase!!.getEventSectorImage(
                eventId = 12,
                sectorId = 1,
                EventSectorImageSubscriber())
        }
    }

    private inner class EventSectorImageSubscriber: BaseSubscriber<SectorImageModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: SectorImageModel) {
            super.onNext(response)
            Log.d("ffd", "EventSectorImageSubscriber")

            eventUseCase!!.getEventSectorSvg(
                eventId = 12,
                sectorId = 1,
                EventSectorSvgSubscriber())
        }
    }

    private inner class EventSectorSvgSubscriber: BaseSubscriber<SectorSvgModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: SectorSvgModel) {
            super.onNext(response)
            Log.d("ffd", "EventSectorSvgSubscriber")

//            orderUseCase!!.getOrders(
//                status = 1,
//                OrdersSubscriber())
        }
    }

}