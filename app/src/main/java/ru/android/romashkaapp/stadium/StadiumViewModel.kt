package ru.android.romashkaapp.stadium

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import ru.android.romashkaapp.base.BaseViewModel

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class StadiumViewModel(application: Application) : BaseViewModel(application), View.OnClickListener{

    val zoomView = MutableLiveData<Boolean>()
    val toolbarView = MutableLiveData<Boolean>()

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
}