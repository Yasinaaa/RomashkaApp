package ru.android.romashkaapp.afisha

import android.app.Application
import android.content.Context
import android.ru.romashkaapp.models.*
import android.ru.romashkaapp.usecases.DictionaryUseCase
import android.ru.romashkaapp.usecases.EventsUseCase
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import ru.android.romashkaapp.BaseSubscriber
import ru.android.romashkaapp.StartActivity
import ru.android.romashkaapp.afisha.adapters.MatchesAdapter
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
    private var dictionaryUseCase: DictionaryUseCase? = null
    val matchesList: MutableLiveData<MutableList<MatchesAdapter.Match?>> = MutableLiveData()
    val servicesList: MutableLiveData<MutableList<EventModel>> = MutableLiveData()
    val viewAllClick: MutableLiveData<Boolean> = MutableLiveData()
    val nextFragmentOpenClick = MutableLiveData<MatchesAdapter.Match?>()

    init {
        servicesList.value = arrayListOf(EventModel())
        dictionaryUseCase = DictionaryUseCase(StartActivity.REPOSITORY, getAccessToken(application.applicationContext)!!)
        eventUseCase = EventsUseCase(StartActivity.REPOSITORY, getAccessToken(application.applicationContext)!!)
    }

    fun getEvents(){
        dictionaryUseCase!!.getNoms(last = null, limit = null, NomsSubscriber())
    }

    private inner class NomsSubscriber: BaseSubscriber<MutableList<NomModel>>() { //MutableList<NomModel>

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<NomModel>) {
            super.onNext(response)
            Log.d("ffd", "NomsSubscriber")
            eventUseCase!!.getEvents(page = null, perPage = null, EventsSubscriber(response))
        }
    }

    private inner class EventsSubscriber: BaseSubscriber<MutableList<EventModel>>{

        var noms: MutableList<NomModel> = mutableListOf()

        constructor(n: MutableList<NomModel>){
            noms = n
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<EventModel>) {
            super.onNext(response)
            Log.d("ffd", "EventsSubscriber")

            val list = mutableListOf<MatchesAdapter.Match?>()
            for (event in response){
                val match = MatchesAdapter.Match()
                match.event = event
                for(n in noms){
                    if(event.nom_id == n.id){
                        match.nomTitle = n.name
                    }
                }
                list.add(match)
            }
            list.sortBy{it?.event?.sdate}
            matchesList.value = list.subList(0, 4)
        }
    }

    override fun onClick(view: View?) {

    }

    fun onViewAllBtnClick(){
        viewAllClick.value = true
    }

    override fun click(item: MatchesAdapter.Match?) {
        nextFragmentOpenClick.value = item!!
    }

    fun getListener(): ItemClickListener{
        return this
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
        }
    }

}