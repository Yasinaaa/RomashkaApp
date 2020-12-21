package ru.android.romashkaapp.matches

import android.app.Application
import android.ru.romashkaapp.models.EventModel
import android.ru.romashkaapp.models.NomModel
import android.ru.romashkaapp.usecases.DictionaryUseCase
import android.ru.romashkaapp.usecases.EventsUseCase
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.android.romashkaapp.BaseSubscriber
import ru.android.romashkaapp.StartActivity
import ru.android.romashkaapp.afisha.adapters.MatchesAdapter
import ru.android.romashkaapp.base.BaseViewModel
import ru.android.romashkaapp.utils.Utils

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
public interface ItemClickListener{
    fun click(item: MatchesAdapter.Match?)
}

class MatchesViewModel(application: Application) : BaseViewModel(application), ItemClickListener {

    private var eventUseCase: EventsUseCase? = null
    private var dictionaryUseCase: DictionaryUseCase? = null

    val matchesList: MutableLiveData<MutableList<MatchesAdapter.Match?>> = MutableLiveData()
    val matchesAndCalendarList: MutableLiveData<MutableList<MatchesAdapter.Match?>> = MutableLiveData()
    val nextFragmentOpenClick = MutableLiveData<MatchesAdapter.Match?>()

    init {
        dictionaryUseCase = DictionaryUseCase(StartActivity.REPOSITORY, Utils.getAccessToken(application.applicationContext)!!)
        eventUseCase = EventsUseCase(StartActivity.REPOSITORY, Utils.getAccessToken(application.applicationContext)!!)
        getEvents()
    }

    private fun getEvents(){
        dictionaryUseCase!!.getNoms(1000, NomsSubscriber())
    }

    private inner class NomsSubscriber(): BaseSubscriber<MutableList<NomModel>>() { //MutableList<NomModel>

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<NomModel>) {
            super.onNext(response)
            Log.d("ffd", "NomsSubscriber")
            eventUseCase!!.getEvents(page = null, perPage = null, EventsSubscriber(response))
        }
    }

    private inner class EventsSubscriber: BaseSubscriber<MutableList<EventModel>> {

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

            if(response.isNotEmpty()) {
                val list = mutableListOf<MatchesAdapter.Match?>()
                for (event in response) {
                    val match = MatchesAdapter.Match()
                    match.event = event
                    for (n in noms) {
                        if (event.nom_id == n.id) {
                            match.nomTitle = n.name
                        }
                    }
                    list.add(match)
                }
                list.sortBy { it?.event?.sdate }
                matchesList.value = list
            }else{
                matchesList.value = null
            }
        }
    }

    fun onCalendarBtnClick(){
        var list = matchesList.value
        if(list != null){
            if (list.size > 0) {
                if (list[0] != null)
                    list.add(0, null)
            }
        }
        matchesList.value = list
    }

    fun onMatchesBtnClick(){
        var list = matchesList.value
        if(list != null) {
            if (list.size > 0) {
                if (list[0] == null)
                    list.removeAt(0)
            }
        }
        matchesList.value = list
    }

    override fun click(item: MatchesAdapter.Match?) {
        nextFragmentOpenClick.value = item
    }

    fun getListener(): ItemClickListener{
        return this
    }

}