package ru.android.romashkaapp.login

import android.app.Application
import android.ru.romashkaapp.models.UserModel
import android.ru.romashkaapp.models.request.UserRequestModel
import android.ru.romashkaapp.usecases.UserUseCase
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import okhttp3.ResponseBody
import ru.android.romashkaapp.BaseSubscriber
import ru.android.romashkaapp.StartActivity
import ru.android.romashkaapp.base.BaseViewModel
import ru.android.romashkaapp.utils.Utils

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

    private var usecase: UserUseCase? = null

    init {
        usecase = UserUseCase(StartActivity.REPOSITORY, Utils.getAccessToken(application.applicationContext)!!)
        createUser()
    }

    fun createUser(){
        var user = UserRequestModel()
        user.email = "bla@gmail.com"
        user.phone = "11111111111"
        user.second = "second"
        user.first = "first"
        user.password = "3435gfdd"
        usecase!!.addUser(user, UserSubscriber())
    }

    private inner class UserSubscriber(): BaseSubscriber<ResponseBody>() {
        override fun onComplete() {
            super.onComplete()
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: ResponseBody) {
            super.onNext(response)
            Log.d("ffd", "ss")
        }
    }

    override fun onClick(view: View?) {
        TODO("Not yet implemented")
    }
}