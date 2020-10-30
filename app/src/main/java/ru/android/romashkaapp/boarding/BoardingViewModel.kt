package ru.android.romashkaapp.boarding

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.android.romashkaapp.base.BaseViewModel

/**
 * Created by yasina on 20.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class BoardingViewModel(application: Application) : BaseViewModel(application), View.OnClickListener{

    val nextClick = MutableLiveData<Boolean>()

    override fun onClick(view: View?) {
        nextClick.value = true
    }


}