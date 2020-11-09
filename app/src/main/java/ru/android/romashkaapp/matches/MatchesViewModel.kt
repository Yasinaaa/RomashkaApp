package ru.android.romashkaapp.matches

import android.ru.romashkaapp.models.EventModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class MatchesViewModel: ViewModel(){

    val matchesList: MutableLiveData<MutableList<EventModel>> = MutableLiveData()

    init {
        matchesList.value = arrayListOf(EventModel(), EventModel(), EventModel(), EventModel(), EventModel())
    }

}