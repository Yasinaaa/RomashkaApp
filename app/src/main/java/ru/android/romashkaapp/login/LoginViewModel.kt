package ru.android.romashkaapp.login

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import ru.android.romashkaapp.base.BaseViewModel

/**
 * Created by yasina on 05.10.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
class LoginViewModel(application: Application) : BaseViewModel(application), View.OnClickListener {

    var login = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var lastname = MutableLiveData<String>()
    var name = MutableLiveData<String>()
    var phone = MutableLiveData<String>()
    val error = MutableLiveData<String>()
    var buttonOpacity = ObservableField<Float>()

    override fun onClick(view: View?) {
        TODO("Not yet implemented")
    }
}