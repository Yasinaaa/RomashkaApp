package ru.android.romashkaapp.matches

import android.ru.romashkaapp.models.EventModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.android.romashkaapp.afisha.adapters.MatchesAdapter

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
public interface ItemClickListener{
    fun click(item: MatchesAdapter.Match?)
}

class MatchesViewModel: ViewModel(), ItemClickListener {

    val matchesList: MutableLiveData<MutableList<MatchesAdapter.Match?>> = MutableLiveData()
    val nextFragmentOpenClick = MutableLiveData<MatchesAdapter.Match?>()

    init {
        onMatchesBtnClick()
    }

    fun onCalendarBtnClick(){
//        matchesList.value = arrayListOf(null, MatchesAdapter(EventModel(), EventModel(), EventModel(), EventModel())
    }

    fun onMatchesBtnClick(){
//        matchesList.value = arrayListOf(EventModel(), EventModel(), EventModel(), EventModel(), EventModel())
    }

    override fun click(item: MatchesAdapter.Match?) {
        nextFragmentOpenClick.value = item
    }

    fun getListener(): ItemClickListener{
        return this
    }

}