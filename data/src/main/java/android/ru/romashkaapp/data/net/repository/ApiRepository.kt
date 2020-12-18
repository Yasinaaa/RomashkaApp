package android.ru.romashkaapp.data.net.repository

import android.ru.romashkaapp.data.BuildConfig
import android.ru.romashkaapp.data.net.api.API
import android.ru.romashkaapp.models.*
import android.ru.romashkaapp.models.request.UserRequestModel
import android.ru.romashkaapp.repository.Repository
import android.util.Log
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * Created by yasina on 26.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
@Singleton
class ApiRepository: Repository {

    private val mAPI: API = api()

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
            .readTimeout(120, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
        return Retrofit.Builder()
            .baseUrl("http://192.168.2.80")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(client)
            .build().create(API::class.java)
    }

    override fun getAppToken(
        appToken: AppToken?
    ): Observable<AppTokenResponse> {
        return mAPI.getAppToken(appToken!!)
    }

    override fun getClientToken(
        clientToken: ClientToken
    ): Observable<ClientTokenResponse> {
        return mAPI.getClientToken(clientToken)
    }

    override fun addUser(user: UserRequestModel, accessToken: String): Observable<ResponseBody> {
        return mAPI.addUser(user, "Bearer $accessToken")
    }

    override fun getEvents(accessToken: String, page: Int?, perPage: Int?): Observable<MutableList<EventModel>> {
        return mAPI.getEvents(accessToken= "Bearer $accessToken", page = page, perPage = perPage)
    }

    override fun getEvents(last: String?, limit: String?,
                           active: Boolean?, unitId: Int?,
                           hallId: Int?, nomId: Int?,
                           actionId: Int?, categoryId: Int?,
                           sdateGt: String?, sdateLs: String?,
                           edateGt: String?, edateLs: String?,
                           type: String?): Observable<MutableList<EventModel>> {
        return mAPI.getEvents(last, limit, active, unitId, hallId, nomId, actionId, categoryId, sdateGt, sdateLs, edateGt, edateLs, type)
    }

    override fun getEvent(accessToken: String, eventId: Int): Observable<EventModel> {
        return mAPI.getEvent(eventId, "Bearer $accessToken")
    }

    override fun getHall(accessToken: String, id: Int): Observable<HallModel> {
        return mAPI.getHall(id, "Bearer $accessToken")
    }

    override fun getNoms(accessToken: String, last: String?, limit: String?): Observable<MutableList<NomModel>> {
        return mAPI.getNoms("Bearer $accessToken", null, null)
    }

    override fun getOrder(orderId: Int, accessToken: String): Observable<OrderModel> {
        return mAPI.getUserOrder(orderId=orderId, accessToken="Bearer $accessToken")
    }

    override fun getAllOrders(accessToken: String): Observable<MutableList<OrderModel>> {
        return mAPI.getAllOrders(accessToken="Bearer $accessToken")
    }

    override fun getEventAreas(
        eventId: Int,
        accessToken: String
    ): Observable<MutableList<AreaModel>> {
        return mAPI.getEventAreas(eventId, "Bearer $accessToken")
    }

    override fun getEventArea(
        eventId: Int,
        areaId: Int,
        accessToken: String
    ): Observable<MutableList<SectorModel>> {
        return mAPI.getEventArea(eventId, areaId, "Bearer $accessToken")
    }

    override fun getEventAreaPlan(
        eventId: Int,
        areaId: Int,
        accessToken: String
    ): Observable<ResponseBody> {
        return mAPI.getEventAreaPlan(eventId, areaId, "svg", "Bearer $accessToken")
    }

    override fun getEventSectorSeats(
        eventId: Int,
        sectorId: String?,
        areaId: Int,
        type: String?,
        accessToken: String
    ): Observable<MutableList<SeatModel>> {
        return mAPI.getEventSectorSeats(eventId = eventId, sectorId = sectorId, areaId = areaId, type = type, "Bearer $accessToken")
    }

    override fun getEventSectorZones(
        eventId: Int,
        areaId: Int,
        accessToken: String
    ): Observable<MutableList<ZoneModel>> {
        return mAPI.getEventSectorZones(eventId, areaId, "Bearer $accessToken")
    }

    override fun getEventZonePlaces(
        eventId: Int,
        areaId: Int,
        accessToken: String
    ): Observable<MutableList<ZoneWithFreePlacesModel>> {
        return mAPI.getEventZonePlaces(eventId, areaId, "Bearer $accessToken")
    }

    override fun getEventSectorStatuses(
        eventId: Int,
        sectorId: Int,
        areaId: Int,
        accessToken: String
    ): Observable<MutableList<StatusModel>> {
        return mAPI.getEventSectorStatuses(eventId, sectorId, areaId, "Bearer $accessToken")
    }

    override fun addToCart(
        eventId: Int,
        areaId: Int,
        sid: String,
        accessToken: String
    ): Observable<OrderIdModel> {
        val sidValue = Sid()
        sidValue.sid = sid
        return mAPI.addToCart(eventId, areaId, sidValue, "Bearer $accessToken")
    }

    override fun deleteSeatFromCart(
        eventId: Int,
        areaId: Int,
        sid: String,
        accessToken: String
    ): Observable<ResponseBody> {
        val sidValue = Sid()
        sidValue.sid = sid
        return mAPI.deleteSeatFromCart(eventId, areaId, sidValue, "Bearer $accessToken")
    }

    override fun payOrder(orderId: Int, accessToken: String): Observable<ResponseBody> {
        return mAPI.payOrder(orderId, "Bearer $accessToken")
    }

    override fun getUserOrderCarts(
        orderId: Int,
        accessToken: String
    ): Observable<MutableList<CartModel>> {
        //Content-Type: application/json; charset=UTF-8
        return mAPI.getUserOrderCarts(orderId, "Bearer $accessToken", "ru")
    }
}