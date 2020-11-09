package ru.android.romashkaapp.matches

import android.ru.romashkaapp.models.EventModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
public interface ItemClickListener{
    fun click(item: EventModel?)
}

class MatchesViewModel: ViewModel(), ItemClickListener {

    val matchesList: MutableLiveData<MutableList<EventModel?>> = MutableLiveData()
    val nextFragmentOpenClick = MutableLiveData<EventModel?>()

    init {
        onMatchesBtnClick()
    }

    fun onCalendarBtnClick(){
        matchesList.value = arrayListOf(null, EventModel(), EventModel(), EventModel(), EventModel())
    }

    fun onMatchesBtnClick(){
        matchesList.value = arrayListOf(EventModel(), EventModel(), EventModel(), EventModel(), EventModel())
    }

    override fun click(item: EventModel?) {
        nextFragmentOpenClick.value = item
    }

    fun getListener(): ItemClickListener{
        return this
    }

}