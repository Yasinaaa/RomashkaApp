package android.ru.romashkaapp.repository

import okhttp3.ResponseBody
import io.reactivex.Observable
import android.ru.romashkaapp.models.*
import retrofit2.http.Query

/**
 * Created by yasina on 29.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
interface Repository {

    fun getUsers(): Observable<MutableList<UserModel>>
    fun getUser(userId: Long): Observable<UserModel>
    fun editUser(userId: Long, user: UserModel): Observable<ResponseBody>
    fun getEvents(last: String?, limit: String?,
                  active: Boolean?, unitId: Int?,
                  hallId: Int?, nomId: Int?,
                  actionId: Int?, categoryId: Int?,
                  sdateGt: String?, sdateLs: String?,
                  edateGt: String?, edateLs: String?,
                  type: String?): Observable<MutableList<EventModel>>
    fun getEvent(eventId: Int): Observable<EventModel>
//    fun getSector(eventId: Int,  sectorId: Int): Observable<SectorModel>
//    fun getSectorPlaces(eventId: Int,  sectorId: Int): Observable<SeatModel>
//    fun getSectorSvg(eventId: Int,  sectorId: Int): Observable<SectorSvgModel>
//    fun getUserOrder(userId: Int, orderId: Int): Observable<OrderModel>
//    fun createOrder(userId: Int, order: OrderModel): Observable<OrderModel>
//    fun addSeatToOrder(userId: Int, orderId: Int, sid: String): Observable<CartModel>
//    fun deleteSeatFromCart(userId: Int, orderId: Int, cartId: Int): Observable<ResponseBody>
//    fun payOrder(orderId: Int): Observable<ResponseBody>
    fun getUnits(last: String?, limit: String?): Observable<MutableList<UnitModel>>
    fun getUnit(id: Int): Observable<UnitModel>
    fun getHalls(last: String?, limit: String?): Observable<MutableList<HallModel>>
    fun getHall(id: Int): Observable<HallModel>
    fun getCities(last: String?, limit: String?): Observable<MutableList<CityModel>>
    fun getCity(id: Int): Observable<CityModel>
    fun getCategories(last: String?, limit: String?): Observable<MutableList<CategoryModel>>
    fun getCategory(categoryId: Int): Observable<CategoryModel>
    fun getActions(): Observable<MutableList<ActionModel>>
    fun getAction(actionId: Int): Observable<ActionModel>
    fun getNoms(last: String?, limit: String?): Observable<MutableList<NomModel>>
    fun getNom(nomId: Int): Observable<NomModel>
    
    fun getEventSubscriptions(eventId: Int): Observable<MutableList<EventModel>>
    fun getEventSector(eventId: Int, sectorId: Int, last: String?, lastSeatsGt: String?, lastAreaGt: String?): Observable<SectorModel>
    fun getEventSectorZones(eventId: Int,sectorId: Int, limit: Int): Observable<MutableList<ZoneModel>>
    fun getEventSectorSeats(eventId: Int,sectorId: Int, limit: Int): Observable<MutableList<SeatModel>>
    fun getEventSectorPoints(eventId: Int,sectorId: Int, limit: Int): Observable<MutableList<PointModel>>
    fun getEventSectorImage(eventId: Int,sectorId: Int): Observable<SectorImageModel>
    fun getEventSectorSvg(eventId: Int,sectorId: Int): Observable<SectorSvgModel>
    fun getUserOrders(status: Int): Observable<MutableList<OrderModel>>
    fun getServices(last: String?, limit: String?, active: Boolean?,unitId: Int?): Observable<MutableList<ServiceModel>>
    fun getService(serviceId: Int): Observable<ServiceModel>
}