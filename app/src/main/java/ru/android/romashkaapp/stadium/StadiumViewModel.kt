package ru.android.romashkaapp.stadium

import android.app.Application
import android.ru.romashkaapp.models.EventModel
import android.view.View
import androidx.lifecycle.MutableLiveData
import ru.android.romashkaapp.base.BaseViewModel
import ru.android.romashkaapp.matches.ItemClickListener

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
public interface ItemClickListener{
    fun click(item: EventModel?)
}

class StadiumViewModel(application: Application) : BaseViewModel(application), View.OnClickListener,
    ItemClickListener {

    val pricesList: MutableLiveData<MutableList<EventModel?>> = MutableLiveData()
    val zoomView = MutableLiveData<Boolean>()
    val toolbarView = MutableLiveData<Boolean>()
    val priceClick = MutableLiveData<Boolean>()

    init {
        pricesList.value = arrayListOf(EventModel(), EventModel(), EventModel(), EventModel(), EventModel())
    }

    fun zoomIn(){
        zoomView.value = true
    }

    fun zoomOut(){
        zoomView.value = false
    }

    fun toolbarView(){
        toolbarView.value = toolbarView.value != true
    }

    override fun onClick(view: View?) {
        TODO("Not yet implemented")
    }

    override fun click(item: EventModel?) {
//        nextFragmentOpenClick.value = item
    }

    fun getListener(): ItemClickListener{
        return this
    }

    fun onPricesClick(){
        priceClick.value = true
    }
}