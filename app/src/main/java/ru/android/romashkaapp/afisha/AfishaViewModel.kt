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
        dictionaryUseCase = DictionaryUseCase(
            StartActivity.REPOSITORY,
            getAccessToken(application.applicationContext)!!
        )
        eventUseCase = EventsUseCase(
            StartActivity.REPOSITORY,
            getAccessToken(application.applicationContext)!!
        )
    }

    fun getEvents() {
        dictionaryUseCase!!.getNoms(perPage = 1000, NomsSubscriber())
    }

    private inner class NomsSubscriber : BaseSubscriber<MutableList<NomModel>>() { //MutableList<NomModel>

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<NomModel>) {
            super.onNext(response)
            Log.d("ffd", "NomsSubscriber")
            eventUseCase!!.getEvents(page = null, perPage = null, EventsSubscriber(response))
        }
    }

    private inner class EventsSubscriber : BaseSubscriber<MutableList<EventModel>> {

        var noms: MutableList<NomModel> = mutableListOf()

        constructor(n: MutableList<NomModel>) {
            noms = n
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<EventModel>) {
            super.onNext(response)
            Log.d("ffd", "EventsSubscriber")

            val list = mutableListOf<MatchesAdapter.Match?>()
            if(response.size > 0 ) {
                for (event in response) {
                    if (event.active!!) {
                        val match = MatchesAdapter.Match()
                        match.event = event
                        for (n in noms) {
                            if (event.nom_id == n.id) {
                                match.nomTitle = n.name
                            }
                        }
                        list.add(match)
                    }
                }
                list.sortBy { it?.event?.sdate }
                if (list.size > 4)
                    matchesList.value = list.subList(0, 4)
                else
                    matchesList.value = list.subList(0, list.size)

                click(list[0])
            }else{
                matchesList.value = null
            }
        }
    }

    override fun onClick(view: View?) {

    }

    fun onViewAllBtnClick() {
        viewAllClick.value = true
    }

    override fun click(item: MatchesAdapter.Match?) {
        nextFragmentOpenClick.value = item!!
    }

    fun getListener(): ItemClickListener {
        return this
    }

}