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

    override fun getUsers(accessToken: String): Observable<MutableList<UserModel>> {
        return mAPI.getUsers(accessToken)
    }

    override fun addUser(user: UserRequestModel, accessToken: String): Observable<ResponseBody> {
        return mAPI.addUser(user, "Bearer " + accessToken)
    }

    override fun getUser(userId: Long): Observable<UserModel> {
        return mAPI.getUser(userId)
    }

    override fun editUser(userId: Long, user: UserModel): Observable<ResponseBody> {
        return mAPI.editUser(userId, user)
    }

    override fun getEvents(accessToken: String, page: Int?, perPage: Int?): Observable<MutableList<EventModel>> {
        return mAPI.getEvents(accessToken= accessToken, page = page, perPage = perPage)
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
        return mAPI.getEvent(eventId, accessToken)
    }

//    override fun getSector(eventId: Int, sectorId: Int): Observable<SectorModel> {
//        return mAPI.getSector(eventId, sectorId)
//    }
//
//    override fun getSectorPlaces(eventId: Int, sectorId: Int): Observable<SeatModel> {
//        return mAPI.getSectorPlaces(eventId, sectorId)
//    }
//
//    override fun getSectorSvg(eventId: Int, sectorId: Int): Observable<SectorSvgModel> {
//        return mAPI.getSectorSvg(eventId, sectorId)
//    }
//
//    override fun getUserOrders(userId: Int): Observable<MutableList<OrderModel>> {
//        return mAPI.getUserOrders(userId)
//    }
//
//    override fun getUserOrder(userId: Int, orderId: Int): Observable<OrderModel> {
//        return mAPI.getUserOrder(userId, orderId)
//    }
//
//    override fun createOrder(userId: Int, order: OrderModel): Observable<OrderModel> {
//        return mAPI.createOrder(userId, order)
//    }
//
//    override fun addSeatToOrder(userId: Int, orderId: Int, sid: String): Observable<CartModel> {
//        return mAPI.addSeatToOrder(userId, orderId, sid)
//    }
//
//    override fun deleteSeatFromCart(
//        userId: Int,
//        orderId: Int,
//        cartId: Int
//    ): Observable<ResponseBody> {
//        return mAPI.deleteSeatFromCart(userId, orderId, cartId)
//    }
//
//    override fun payOrder(orderId: Int): Observable<ResponseBody> {
//        return mAPI.payOrder(orderId)
//    }

    override fun getUnits(last: String?, limit: String?): Observable<MutableList<UnitModel>> {
        return mAPI.getUnits(last, limit)
    }

    override fun getUnit(id: Int): Observable<UnitModel> {
        return mAPI.getUnit(id)
    }

    override fun getHalls(last: String?, limit: String?): Observable<MutableList<HallModel>> {
        return mAPI.getHalls(last, limit)
    }

    override fun getHall(accessToken: String, id: Int): Observable<HallModel> {
        return mAPI.getHall(id, accessToken)
    }

    override fun getCities(last: String?, limit: String?): Observable<MutableList<CityModel>> {
        return mAPI.getCities(last, limit)
    }

    override fun getCity(id: Int): Observable<CityModel> {
        return mAPI.getCity(id)
    }

    override fun getCategories(accessToken: String, last: String?, limit: String?): Observable<MutableList<CategoryModel>> {
        return mAPI.getCategories(accessToken, last, limit)
    }

    override fun getCategory(categoryId: Int): Observable<CategoryModel> {
        return mAPI.getCategory(categoryId)
    }

    override fun getActions(accessToken: String): Observable<MutableList<ActionModel>> {
        return mAPI.getActions(accessToken)
    }

    override fun getAction(id: Int): Observable<ActionModel> {
        return mAPI.getAction(id)
    }

    override fun getNoms(accessToken: String, last: String?, limit: String?): Observable<MutableList<NomModel>> {
        return mAPI.getNoms(accessToken, null, null)
    }

    override fun getNom(nomId: Int): Observable<NomModel> {
        return mAPI.getNom(nomId)
    }

    override fun getEventSubscriptions(eventId: Int): Observable<MutableList<EventModel>> {
        return mAPI.getEventSubscriptions(eventId)
    }

    override fun getEventSector(
        eventId: Int,
        sectorId: Int,
        last: String?,
        lastSeatsGt: String?,
        lastAreaGt: String?
    ): Observable<SectorModel> {
        return mAPI.getEventSector(eventId, sectorId, last, lastSeatsGt, lastAreaGt)
    }

    override fun getEventSectorPoints(
        eventId: Int,
        sectorId: Int,
        limit: Int
    ): Observable<MutableList<PointModel>> {
        return mAPI.getEventSectorPoints(eventId, sectorId, limit)
    }

    override fun getUserOrders(status: Int): Observable<MutableList<OrderModel>> {
        return mAPI.getUserOrders(status)
    }

    override fun getServices(
        last: String?,
        limit: String?,
        active: Boolean?,
        unitId: Int?
    ): Observable<MutableList<ServiceModel>> {
        return mAPI.getServices(last, limit, active, unitId)
    }

    override fun getService(serviceId: Int): Observable<ServiceModel> {
        return mAPI.getService(serviceId)
    }

    override fun getEventAreas(
        eventId: Int,
        accessToken: String
    ): Observable<MutableList<AreaModel>> {
        return mAPI.getEventAreas(eventId, accessToken)
    }

    override fun getEventArea(
        eventId: Int,
        areaId: Int,
        accessToken: String
    ): Observable<MutableList<SectorModel>> {
        return mAPI.getEventArea(eventId, areaId, accessToken)
    }

    override fun getEventAreaPlan(
        eventId: Int,
        areaId: Int,
        accessToken: String
    ): Observable<ResponseBody> {
        return mAPI.getEventAreaPlan(eventId, areaId, "svg", accessToken)
    }

    override fun getEventSectorSeats(
        eventId: Int,
        sectorId: String?,
        areaId: Int,
        type: String?,
        accessToken: String
    ): Observable<MutableList<SeatModel>> {
        return mAPI.getEventSectorSeats(eventId, sectorId, areaId, type, accessToken)
    }

    override fun getEventSectorZones(
        eventId: Int,
        areaId: Int,
        accessToken: String
    ): Observable<MutableList<ZoneModel>> {
        return mAPI.getEventSectorZones(eventId, areaId, accessToken)
    }

    override fun getEventZonePlaces(
        eventId: Int,
        areaId: Int,
        accessToken: String
    ): Observable<MutableList<ZoneWithFreePlacesModel>> {
        return mAPI.getEventZonePlaces(eventId, areaId, accessToken)
    }

    override fun getEventSectorStatuses(
        eventId: Int,
        sectorId: Int,
        areaId: Int,
        accessToken: String
    ): Observable<MutableList<StatusModel>> {
        return mAPI.getEventSectorStatuses(eventId, sectorId, areaId, accessToken)
    }

    override fun addToCart(
        eventId: Int,
        areaId: Int,
        sid: String,
        accessToken: String
    ): Observable<ResponseBody> {
        val sidValue = Sid()
        sidValue.sid = sid
        return mAPI.addToCart(eventId, areaId, sidValue, accessToken)
    }
}