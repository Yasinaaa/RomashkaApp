package ru.android.romashkaapp.afisha

import android.app.Application
import android.ru.romashkaapp.models.EventModel
import android.view.View
import androidx.lifecycle.MutableLiveData
import ru.android.romashkaapp.base.BaseViewModel

/**
 * Created by yasina on 02.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class AfishaViewModel(application: Application) : BaseViewModel(application), View.OnClickListener{

    val matchesList: MutableLiveData<MutableList<EventModel>> = MutableLiveData()
    val servicesList: MutableLiveData<MutableList<EventModel>> = MutableLiveData()
    val viewAllClick: MutableLiveData<Boolean> = MutableLiveData()

    init {
        matchesList.value = arrayListOf(EventModel(), EventModel(), EventModel())
        servicesList.value = arrayListOf(EventModel())
    }

    override fun onClick(view: View?) {

    }

    fun onViewAllBtnClick(){
        viewAllClick.value = true
    }
}