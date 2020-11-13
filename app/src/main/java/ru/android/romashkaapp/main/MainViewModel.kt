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
import ru.android.romashkaapp.afisha.AfishaFragment
import ru.android.romashkaapp.base.BaseViewModel
import ru.android.romashkaapp.matches.MatchesFragment
import ru.android.romashkaapp.sector_seat.SectorSeatFragment
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
    private var eventUseCase: EventsUseCase? = null
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

    private fun api(): API {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor { chain ->
                val request = chain.request()
                var response = chain.proceed(request)
                var tryCount = 0
                while (!response.isSuccessful && tryCount < 3) {
                    Log.d("intercept", "Request is not successful - $tryCount")
                    tryCount++
                    response = chain.proceed(request)
                }
                response
            }.connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
        return Retrofit.Builder()
            .baseUrl("http://192.168.2.49:23400") //33100
//            .baseUrl("https://private-a905e4-artemshvedenko.apiary-mock.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(client)
            .build().create(API::class.java)
    }

    init{
        val repository = ApiRepository(api())
        usecase = UserUseCase(repository)
        usecase!!.getAppToken(clientId = "testclient", clientSecret = "testpass", grantType = "client_credentials", AppTokenSubscriber())
//        usecase!!.getClientToken(clientId = "testclient", clientSecret = "testpass", grantType = "client_credentials",
//            password = "Ls5112233", username = "aa@gmail.com", useCaseDisposable = ClientTokenSubscriber()
//        )
//        usecase!!.getUser(UserSubscriber())

        eventUseCase = EventsUseCase(repository)
        orderUseCase = OrderUseCase(repository)
        dictionaryUseCase = DictionaryUseCase(repository)

        var fragment = AfishaFragment()
        fragment.setViewModel(this)
        createFragment.value = fragment
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

    private inner class AppTokenSubscriber: BaseSubscriber<AppTokenResponse>() {
        override fun onComplete() {
            super.onComplete()
            usecase!!.getClientToken(clientId = "testclient", clientSecret = "testpass", grantType = "password",
                password = "Ls5112233", username = "aa@gmail.com", useCaseDisposable = ClientTokenSubscriber()
            )
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: AppTokenResponse) {
            super.onNext(response)
            Log.d("ffd", "ss=${response.access_token}")
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
            dictionaryUseCase!!.getNoms(accessToken=accessToken!!, last = "100", limit = "100", NomsSubscriber())
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

    private inner class NomsSubscriber(): BaseSubscriber<MutableList<NomModel>>() { //MutableList<NomModel>

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<NomModel>) {
            super.onNext(response)
            Log.d("ffd", "NomsSubscriber")

//            dictionaryUseCase!!.getNom(response[0].id, NomSubscriber())
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

            dictionaryUseCase!!.getHall(response[0].id, HallSubscriber())
        }
    }

    private inner class HallSubscriber: BaseSubscriber<HallModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: HallModel) {
            super.onNext(response)
            Log.d("ffd", "HallSubscriber")

            dictionaryUseCase!!.getServices(last = response.last, limit = "100",
                active = true, unitId = response.unity_id,
                ServicesSubscriber())
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

            eventUseCase!!.getEvents(last = response.last, limit = "100",
                active = true, unitId = response.unit_id, hallId = null, nomId = null,
                actionId = null, categoryId = null, sdateGt = null, sdateLs = null,
                edateGt = null, edateLs = null, type = null,
                EventsSubscriber())
        }
    }

    private inner class EventsSubscriber: BaseSubscriber<MutableList<EventModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<EventModel>) {
            super.onNext(response)
            Log.d("ffd", "EventsSubscriber")

            eventUseCase!!.getEvent(
                response[0].id,
                EventSubscriber())
        }
    }

    private inner class EventSubscriber: BaseSubscriber<EventModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: EventModel) {
            super.onNext(response)
            Log.d("ffd", "EventSubscriber")

            eventUseCase!!.getEventSubscriptions(
                response.id,
                EventSubscriptionsSubscriber())
        }
    }

    private inner class EventSubscriptionsSubscriber: BaseSubscriber<MutableList<EventModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<EventModel>) {
            super.onNext(response)
            Log.d("ffd", "EventSubscriptionsSubscriber")

            eventUseCase!!.getEventSector(
                eventId = response[0].id,
                sectorId = 1,
                last= "100",
                lastSeatsGt = "100",
                lastAreaGt = "100",
                EventSectorZonesSubscriber())
        }
    }

    private inner class EventSectorZonesSubscriber: BaseSubscriber<SectorModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: SectorModel) {
            super.onNext(response)
            Log.d("ffd", "EventSectorZonesSubscriber")

            eventUseCase!!.getEventSectorSeats(
                eventId = response.id,
                sectorId = 1,
                limit = 100,
                EventSectorSeatsSubscriber())
        }
    }

    private inner class EventSectorSeatsSubscriber: BaseSubscriber<MutableList<SeatModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<SeatModel>) {
            super.onNext(response)
            Log.d("ffd", "EventSectorSeatsSubscriber")

            eventUseCase!!.getEventSectorPoints(
                eventId = 12,
                sectorId = 1,
                limit = 100,
                EventSectorPointsSubscriber())
        }
    }

    private inner class EventSectorPointsSubscriber: BaseSubscriber<MutableList<PointModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<PointModel>) {
            super.onNext(response)
            Log.d("ffd", "EventSectorPointsSubscriber")

            eventUseCase!!.getEventSectorImage(
                eventId = 12,
                sectorId = 1,
                EventSectorImageSubscriber())
        }
    }

    private inner class EventSectorImageSubscriber: BaseSubscriber<SectorImageModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: SectorImageModel) {
            super.onNext(response)
            Log.d("ffd", "EventSectorImageSubscriber")

            eventUseCase!!.getEventSectorSvg(
                eventId = 12,
                sectorId = 1,
                EventSectorSvgSubscriber())
        }
    }

    private inner class EventSectorSvgSubscriber: BaseSubscriber<SectorSvgModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: SectorSvgModel) {
            super.onNext(response)
            Log.d("ffd", "EventSectorSvgSubscriber")

            orderUseCase!!.getOrders(
                status = 1,
                OrdersSubscriber())
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
}