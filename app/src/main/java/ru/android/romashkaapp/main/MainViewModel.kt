package ru.android.romashkaapp.main

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Environment
import android.os.SystemClock
import android.ru.romashkaapp.data.net.api.API
import android.ru.romashkaapp.data.net.repository.ApiRepository
import android.ru.romashkaapp.models.*
import android.ru.romashkaapp.usecases.DictionaryUseCase
import android.ru.romashkaapp.usecases.EventsUseCase
import android.ru.romashkaapp.usecases.OrderUseCase
import android.ru.romashkaapp.usecases.UserUseCase
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.android.romashkaapp.BaseSubscriber
import ru.android.romashkaapp.BuildConfig
import ru.android.romashkaapp.StartActivity
import ru.android.romashkaapp.StartActivity.Companion.REPOSITORY
import ru.android.romashkaapp.afisha.AfishaFragment
import ru.android.romashkaapp.base.BaseViewModel
import ru.android.romashkaapp.matches.MatchesFragment
import ru.android.romashkaapp.sector_seat.SectorSeatFragment
import ru.android.romashkaapp.utils.Utils
import ru.android.romashkaapp.utils.Utils.Companion.CLIENT_ID
import ru.android.romashkaapp.utils.Utils.Companion.CLIENT_SECRET
import ru.android.romashkaapp.utils.Utils.Companion.GRANT_TYPE
import ru.android.romashkaapp.utils.Utils.Companion.saveAccessToken
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.util.concurrent.TimeUnit

/**
 * Created by yasina on 01.10.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
class MainViewModel(application: Application) : BaseViewModel(application), View.OnClickListener{

    override fun onClick(view: View?) {
        TODO("Not yet implemented")
    }

    private var usecase: UserUseCase? = null
    private var dictionaryUseCase: DictionaryUseCase? = null

    private var orderUseCase: OrderUseCase? = null
    val picture = MutableLiveData<String>()
    val createFragment = MutableLiveData<Fragment>()
    val bottomBar = MutableLiveData<Boolean>()
    var accessToken: String? = null

    fun skipNavigationBar(){
        bottomBar.value = false
    }

    fun showNavigationBar(){
        bottomBar.value = true
    }

    init{
        usecase = UserUseCase(REPOSITORY, Utils.getAccessToken(application.applicationContext))
        //step 1
        usecase!!.getAppToken(clientId = CLIENT_ID, clientSecret = CLIENT_SECRET, grantType = GRANT_TYPE, AppTokenSubscriber())


        orderUseCase = OrderUseCase(REPOSITORY, Utils.getAccessToken(application.applicationContext))
//        dictionaryUseCase = DictionaryUseCase(REPOSITORY)
    }

    fun setMatchesFragment(){
        var fragment = MatchesFragment()
        fragment.setViewModel(this)
        createFragment.value = fragment
    }

    fun setSectorFragment(){
        var fragment = SectorSeatFragment()
//        fragment.setViewModel(this)
        createFragment.value = fragment
    }

    private inner class AppTokenSubscriber: BaseSubscriber<AppTokenResponse>() {
        override fun onComplete() {
            super.onComplete()
//            usecase!!.getClientToken(clientId = "testclient", clientSecret = "testpass", grantType = "password",
//                password = "Ls5112233", username = "aa@gmail.com", useCaseDisposable = ClientTokenSubscriber()
//            )
        }

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
            Log.d("ffd", "ss=$response")
            usecase!!.getUsers(response.access_token!!, AllUsersSubscriber())
            accessToken = response.access_token
        }
    }

    private inner class UserSubscriber(): BaseSubscriber<UserModel>() {
        override fun onComplete() {
            super.onComplete()
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: UserModel) {
            super.onNext(response)
            Log.d("ffd", response.email)

            var id = response.id
            var user = UserModel()
            user.photo = "R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw=="

            usecase!!.editUser(id, user, PatchUserSubscriber())
        }
    }

    private inner class PatchUserSubscriber(): BaseSubscriber<ResponseBody>() {
        override fun onComplete() {
            super.onComplete()
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: ResponseBody) {
            super.onNext(response)
            Log.d("ffd", "ss")
            picture.value = "R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw=="

        }
    }

    private inner class AllUsersSubscriber(): BaseSubscriber<MutableList<UserModel>>() {
        override fun onComplete() {
            super.onComplete()
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<UserModel>) {
            super.onNext(response)
            Log.d("ffd", "fd")
//            dictionaryUseCase!!.getCategories(accessToken!!, last = null, limit = "100", CategoriesSubscriber())
        }
    }

    private inner class CategoriesSubscriber: BaseSubscriber<MutableList<CategoryModel>>() {
        override fun onComplete() {
            super.onComplete()
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<CategoryModel>) {
            super.onNext(response)
            Log.d("ffd", "CategoriesSubscriber")
//            dictionaryUseCase!!.getActions(accessToken!!, ActionsSubscriber())

//            dictionaryUseCase!!.getCategory(response[0].id, CategorySubscriber())
        }
    }

    private inner class CategorySubscriber: BaseSubscriber<CategoryModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: CategoryModel) {
            super.onNext(response)
            Log.d("ffd", "CategorySubscriber")


        }
    }

    private inner class ActionsSubscriber: BaseSubscriber<MutableList<ActionModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<ActionModel>) {
            super.onNext(response)
            Log.d("ffd", "ActionsSubscriber")
//            dictionaryUseCase!!.getNoms(accessToken=accessToken!!, last = response.last().last, limit = "100", NomsSubscriber())
//            dictionaryUseCase!!.getAction(response[0].id, ActionSubscriber())
        }
    }

    private inner class ActionSubscriber: BaseSubscriber<ActionModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: ActionModel) {
            super.onNext(response)
            Log.d("ffd", "ActionSubscriber")

//            dictionaryUseCase!!.getNoms(last = response.last, limit = "100", NomsSubscriber())
        }
    }



    private inner class NomSubscriber: BaseSubscriber<NomModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: NomModel) {
            super.onNext(response)
            Log.d("ffd", "CitiesSubscriber")

            dictionaryUseCase!!.getCities(last = response.last, limit = "100", CitiesSubscriber())
        }
    }

    private inner class CitiesSubscriber(): BaseSubscriber<MutableList<CityModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<CityModel>) {
            super.onNext(response)
            Log.d("ffd", "CitySubscriber")

            dictionaryUseCase!!.getCity(response[0].id, CitySubscriber())
        }
    }

    private inner class CitySubscriber: BaseSubscriber<CityModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: CityModel) {
            super.onNext(response)
            Log.d("ffd", "CitySubscriber")

            dictionaryUseCase!!.getUnits(last = response.last, limit = "100", UnitsSubscriber())
        }
    }

    private inner class UnitsSubscriber(): BaseSubscriber<MutableList<UnitModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<UnitModel>) {
            super.onNext(response)
            Log.d("ffd", "UnitSubscriber")

            dictionaryUseCase!!.getUnit(response[0].id, UnitSubscriber())
        }
    }

    private inner class UnitSubscriber: BaseSubscriber<UnitModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: UnitModel) {
            super.onNext(response)
            Log.d("ffd", "UnitSubscriber")

            dictionaryUseCase!!.getHalls(last = response.last, limit = "100", HallsSubscriber())
        }
    }

    private inner class HallsSubscriber(): BaseSubscriber<MutableList<HallModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<HallModel>) {
            super.onNext(response)
            Log.d("ffd", "UnitSubscriber")

        }
    }

    private inner class ServicesSubscriber: BaseSubscriber<MutableList<ServiceModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<ServiceModel>) {
            super.onNext(response)
            Log.d("ffd", "ServicesSubscriber")

            dictionaryUseCase!!.getService(response[0].id, ServiceSubscriber())
        }
    }

    private inner class ServiceSubscriber: BaseSubscriber<ServiceModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: ServiceModel) {
            super.onNext(response)
            Log.d("ffd", "ServiceSubscriber")
        }
    }

    private inner class OrdersSubscriber: BaseSubscriber<MutableList<OrderModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<OrderModel>) {
            super.onNext(response)
            Log.d("ffd", "OrdersSubscriber")
        }
    }

    fun saveSvgToLocal(context: Context){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "please grant write file permission and trya gain", Toast.LENGTH_SHORT).show()
        } else {
//            val dir = File(Environment.getExternalStorageDirectory(), "dataset")
//            val file = File(dir, "sector_${SystemClock.currentThreadTimeMillis()}.svg")
//            try {
//                // response is the data written to file
//                PrintWriter(file).use { out -> out.println("gdsgds") }
//            } catch (e: Exception) {
//                // handle the exception
//            }
            val path = context.getExternalFilesDir(null)
            val letDirectory = File(path, "LET")
            letDirectory.mkdirs()
            val file = File(letDirectory, "Records.svg")
            FileOutputStream(file).use {
                it.write("record goes here".encodeToByteArray())
            }
        }
    }
}