package ru.android.romashkaapp.main

import android.app.Application
import android.os.Bundle
import android.ru.romashkaapp.models.*
import android.ru.romashkaapp.usecases.DictionaryUseCase
import android.ru.romashkaapp.usecases.OrderUseCase
import android.ru.romashkaapp.usecases.UserUseCase
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import okhttp3.ResponseBody
import ru.android.romashkaapp.BaseSubscriber
import ru.android.romashkaapp.StartActivity.Companion.REPOSITORY
import ru.android.romashkaapp.afisha.AfishaFragment
import ru.android.romashkaapp.base.BaseViewModel
import ru.android.romashkaapp.matches.MatchesFragment
import ru.android.romashkaapp.sector_seat.SectorSeatFragment
import ru.android.romashkaapp.stadium.StadiumFragment
import ru.android.romashkaapp.utils.Utils
import ru.android.romashkaapp.utils.Utils.Companion.CLIENT_ID
import ru.android.romashkaapp.utils.Utils.Companion.CLIENT_SECRET
import ru.android.romashkaapp.utils.Utils.Companion.GRANT_TYPE
import ru.android.romashkaapp.utils.Utils.Companion.saveAccessToken
import ru.android.romashkaapp.utils.Utils.Companion.saveUserToken

/**
 * Created by yasina on 01.10.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
class MainViewModel(application: Application) : BaseViewModel(application), View.OnClickListener{

    override fun onClick(view: View?) {
        TODO("Not yet implemented")
    }

    private var userUseCase: UserUseCase? = null
    private var dictionaryUseCase: DictionaryUseCase? = null

    private var orderUseCase: OrderUseCase? = null
    val picture = MutableLiveData<String>()
    val createFragment = MutableLiveData<Fragment>()
    val bottomBar = MutableLiveData<Boolean>()
    var accessToken: String? = null

    fun hideNavigationBar(){
        bottomBar.value = false
    }

    fun showNavigationBar(){
        bottomBar.value = true
    }

    init{
        userUseCase = UserUseCase(REPOSITORY, Utils.getAccessToken(application.applicationContext))
        //step 1
//        if(!Utils.isUserLogined(application.applicationContext)) {
//            userUseCase!!.getAppToken(
//                clientId = CLIENT_ID,
//                clientSecret = CLIENT_SECRET,
//                grantType = GRANT_TYPE,
//                AppTokenSubscriber()
//            )
//        }else{
            userUseCase!!.getClientToken(clientId = Utils.CLIENT_ID_USER, clientSecret = Utils.CLIENT_SECRET_USER,
                grantType = Utils.GRANT_TYPE_PASSWORD,
                username = "bla435@gmail.com",
                password = "3435gfdd",
                scope = Utils.CLIENT_SCOPES,
                ClientTokenSubscriber())
//        }

        orderUseCase = OrderUseCase(REPOSITORY, Utils.getAccessToken(application.applicationContext))
//        dictionaryUseCase = DictionaryUseCase(REPOSITORY)
    }

    fun setMatchesFragment(){
        var fragment = MatchesFragment()
        fragment.setViewModel(this)
        createFragment.value = fragment
    }

    fun setStadiumFragment(bundle: Bundle){
        var fragment = StadiumFragment()
        fragment.arguments = bundle
        fragment.setViewModel(this)
        createFragment.value = fragment
    }

    private inner class AppTokenSubscriber: BaseSubscriber<AppTokenResponse>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: AppTokenResponse) {
            super.onNext(response)
            Log.d("ffd", "ss=${response.access_token}")
            saveAccessToken(context, response.access_token)

            var fragment = AfishaFragment()
            fragment.setViewModel(this@MainViewModel)
            createFragment.value = fragment
        }
    }

    private inner class ClientTokenSubscriber: BaseSubscriber<ClientTokenResponse>() {
        override fun onComplete() {
            super.onComplete()
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: ClientTokenResponse) {
            super.onNext(response)

            Log.d("ffd", "ss=${response.access_token}")
            saveUserToken(context, response.access_token)

            var fragment = AfishaFragment()
            fragment.setViewModel(this@MainViewModel)
            createFragment.value = fragment
        }
    }

//    private inner class ServicesSubscriber: BaseSubscriber<MutableList<ServiceModel>>() {
//
//        override fun onError(e: Throwable) {
//            super.onError(e)
//        }
//
//        override fun onNext(response: MutableList<ServiceModel>) {
//            super.onNext(response)
//            Log.d("ffd", "ServicesSubscriber")
//
//            dictionaryUseCase!!.getService(response[0].id, ServiceSubscriber())
//        }
//    }
//
//    private inner class ServiceSubscriber: BaseSubscriber<ServiceModel>() {
//
//        override fun onError(e: Throwable) {
//            super.onError(e)
//        }
//
//        override fun onNext(response: ServiceModel) {
//            super.onNext(response)
//            Log.d("ffd", "ServiceSubscriber")
//        }
//    }

}