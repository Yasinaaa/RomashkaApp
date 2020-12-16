package android.ru.romashkaapp.repository

import okhttp3.ResponseBody
import io.reactivex.Observable
import android.ru.romashkaapp.models.*
import android.ru.romashkaapp.models.request.UserRequestModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by yasina on 29.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
interface Repository {

    fun getAppToken(appToken: AppToken?): Observable<AppTokenResponse>
    fun getClientToken(clientToken: ClientToken): Observable<ClientTokenResponse>
    fun addUser(user: UserRequestModel, accessToken: String): Observable<ResponseBody>
    fun getUsers(accessToken: String): Observable<MutableList<UserModel>>
    fun getUser(userId: Long): Observable<UserModel>
    fun editUser(userId: Long, user: UserModel): Observable<ResponseBody>
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
//    fun getUserOrder(userId: Int, orderId: Int): Observable<OrderModel>
//    fun createOrder(userId: Int, order: OrderModel): Observable<OrderModel>
//    fun addSeatToOrder(userId: Int, orderId: Int, sid: String): Observable<CartModel>
//    fun deleteSeatFromCart(userId: Int, orderId: Int, cartId: Int): Observable<ResponseBody>
//    fun payOrder(orderId: Int): Observable<ResponseBody>
    fun getUnits(last: String?, limit: String?): Observable<MutableList<UnitModel>>
    fun getUnit(id: Int): Observable<UnitModel>
    fun getHalls(last: String?, limit: String?): Observable<MutableList<HallModel>>
    fun getHall(accessToken: String, id: Int): Observable<HallModel>
    fun getCities(last: String?, limit: String?): Observable<MutableList<CityModel>>
    fun getCity(id: Int): Observable<CityModel>
    fun getCategories(accessToken: String, last: String?, limit: String?): Observable<MutableList<CategoryModel>>
    fun getCategory(categoryId: Int): Observable<CategoryModel>
    fun getActions(accessToken: String): Observable<MutableList<ActionModel>>
    fun getAction(actionId: Int): Observable<ActionModel>
    fun getNoms(accessToken: String, last: String?, limit: String?): Observable<MutableList<NomModel>>
    fun getNom(nomId: Int): Observable<NomModel>
    
    fun getEventSubscriptions(eventId: Int): Observable<MutableList<EventModel>>
    fun getEventSector(eventId: Int, sectorId: Int, last: String?, lastSeatsGt: String?, lastAreaGt: String?): Observable<SectorModel>
    fun getEventSectorPoints(eventId: Int, sectorId: Int, limit: Int): Observable<MutableList<PointModel>>

    fun getOrder(orderId:Int, accessToken: String): Observable<OrderModel>
    fun getAllOrders(accessToken: String): Observable<MutableList<OrderModel>>
    fun getServices(last: String?, limit: String?, active: Boolean?,unitId: Int?): Observable<MutableList<ServiceModel>>
    fun getService(serviceId: Int): Observable<ServiceModel>
}