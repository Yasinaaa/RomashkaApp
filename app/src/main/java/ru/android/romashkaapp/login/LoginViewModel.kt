package ru.android.romashkaapp.login

import android.app.Application
import android.ru.romashkaapp.models.ClientTokenResponse
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

    private var userUseCase: UserUseCase? = null

    init {
        userUseCase = UserUseCase(StartActivity.REPOSITORY, Utils.getAccessToken(application.applicationContext)!!)
        createUser()
    }

    fun createUser(){
        var user = UserRequestModel()
        user.email = "radikPadlo1@gmail.com"
        user.phone = "11111111111"
        user.second = "second"
        user.first = "first"
        user.password = "3435gfdd"
        userUseCase!!.addUser(user, UserSubscriber())
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

            userUseCase!!.getClientToken(clientId = Utils.CLIENT_ID_USER, clientSecret = Utils.CLIENT_SECRET_USER,
                grantType = Utils.GRANT_TYPE_PASSWORD,
                username = "bla435@gmail.com",
                password = "3435gfdd",
                scope = Utils.CLIENT_SCOPES,
                ClientTokenSubscriber())
        }
    }

    private inner class ClientTokenSubscriber: BaseSubscriber<ClientTokenResponse>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: ClientTokenResponse) {
            super.onNext(response)
            Log.d("ffd", "ss=$response")
            Utils.saveUserToken(context, response.access_token)
        }
    }

    override fun onClick(view: View?) {
        TODO("Not yet implemented")
    }
}