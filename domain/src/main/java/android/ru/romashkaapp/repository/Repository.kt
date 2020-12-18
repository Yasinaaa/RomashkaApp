package android.ru.romashkaapp.repository

import okhttp3.ResponseBody
import io.reactivex.Observable
import android.ru.romashkaapp.models.*
import android.ru.romashkaapp.models.request.UserRequestModel
import retrofit2.http.*

/**
 * Created by yasina on 29.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
interface Repository {

    fun getAppToken(appToken: AppToken?): Observable<AppTokenResponse>
    fun getClientToken(clientToken: ClientToken): Observable<ClientTokenResponse>
    fun addUser(user: UserRequestModel, accessToken: String): Observable<ResponseBody>
    fun getEvents(last: String?, limit: String?,
                  active: Boolean?, unitId: Int?,
                  hallId: Int?, nomId: Int?,
                  actionId: Int?, categoryId: Int?,
                  sdateGt: String?, sdateLs: String?,
                  edateGt: String?, edateLs: String?,
                  type: String?): Observable<MutableList<EventModel>>
    fun getEvent(accessToken: String, eventId: Int): Observable<EventModel>
    fun getEvents(accessToken: String, page: Int?, perPage: Int?): Observable<MutableList<EventModel>>
    fun getEventAreas(eventId: Int, accessToken: String): Observable<MutableList<AreaModel>>
    fun getEventArea(eventId: Int, areaId: Int, accessToken: String): Observable<MutableList<SectorModel>>
    fun getEventAreaPlan(eventId: Int, areaId: Int, accessToken: String): Observable<ResponseBody>
    fun getEventSectorSeats(eventId: Int, sectorId: String?, areaId: Int, type: String?, accessToken: String): Observable<MutableList<SeatModel>>
    fun getEventSectorZones(eventId: Int, areaId: Int, accessToken: String): Observable<MutableList<ZoneModel>>
    fun getEventZonePlaces(eventId: Int, areaId: Int, accessToken: String): Observable<MutableList<ZoneWithFreePlacesModel>>
    fun getEventSectorStatuses(eventId: Int, sectorId: Int, areaId: Int, accessToken: String): Observable<MutableList<StatusModel>>
    fun addToCart(eventId: Int, areaId: Int, sid: String, accessToken: String): Observable<OrderIdModel>
    fun deleteSeatFromCart(eventId: Int, areaId: Int, sid: String, accessToken: String): Observable<ResponseBody>
    fun getUserOrderCarts(orderId: Int, accessToken: String): Observable<MutableList<CartModel>>
    fun payOrder(orderId: Int, accessToken: String): Observable<ResponseBody>
    fun getHall(accessToken: String, id: Int): Observable<HallModel>
    fun getNoms(accessToken: String, last: String?, limit: String?): Observable<MutableList<NomModel>>
    fun getOrder(orderId:Int, accessToken: String): Observable<OrderModel>
    fun getAllOrders(accessToken: String): Observable<MutableList<OrderModel>>
//    fun getServices(last: String?, limit: String?, active: Boolean?,unitId: Int?): Observable<MutableList<ServiceModel>>
//    fun getService(serviceId: Int): Observable<ServiceModel>
}