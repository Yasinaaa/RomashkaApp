package ru.android.romashkaapp.sector_seat

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import ru.android.romashkaapp.base.BaseViewModel

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class SectorSeatViewModel(application: Application) : BaseViewModel(application), View.OnClickListener{

    val zoomView = MutableLiveData<Boolean>()

    fun zoomIn(){
        zoomView.value = true
    }

    fun zoomOut(){
        zoomView.value = false
    }

    override fun onClick(view: View?) {
        TODO("Not yet implemented")
    }
}