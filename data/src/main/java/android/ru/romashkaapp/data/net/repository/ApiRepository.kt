package android.ru.romashkaapp.data.net.repository

import android.ru.romashkaapp.data.net.api.API
import android.ru.romashkaapp.models.*
import android.ru.romashkaapp.repository.Repository
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Query


/**
 * Created by yasina on 26.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
//@Singleton
class ApiRepository constructor(private val mAPI: API) : Repository {

    override fun getUsers(): Observable<MutableList<UserModel>> {
        return mAPI.getUsers()
    }
    
    override fun getUser(userId: Long): Observable<UserModel> {
        return mAPI.getUser(userId)
    }

    override fun editUser(userId: Long, user: UserModel): Observable<ResponseBody> {
        return mAPI.editUser(userId, user)
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

    override fun getEvent(eventId: Int): Observable<EventModel> {
        return mAPI.getEvent(eventId)
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

    override fun getHall(id: Int): Observable<HallModel> {
        return mAPI.getHall(id)
    }

    override fun getCities(last: String?, limit: String?): Observable<MutableList<CityModel>> {
        return mAPI.getCities(last, limit)
    }

    override fun getCity(id: Int): Observable<CityModel> {
        return mAPI.getCity(id)
    }

    override fun getCategories(last: String?, limit: String?): Observable<MutableList<CategoryModel>> {
        return mAPI.getCategories(last, limit)
    }

    override fun getCategory(categoryId: Int): Observable<CategoryModel> {
        return mAPI.getCategory(categoryId)
    }

    override fun getActions(): Observable<MutableList<ActionModel>> {
        return mAPI.getActions()
    }

    override fun getAction(id: Int): Observable<ActionModel> {
        return mAPI.getAction(id)
    }

    override fun getNoms(last: String?, limit: String?): Observable<MutableList<NomModel>> {
        return mAPI.getNoms(last, limit)
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

    override fun getEventSectorZones(
        eventId: Int,
        sectorId: Int,
        limit: Int
    ): Observable<MutableList<ZoneModel>> {
        return mAPI.getEventSectorZones(eventId, sectorId, limit)
    }

    override fun getEventSectorSeats(
        eventId: Int,
        sectorId: Int,
        limit: Int
    ): Observable<MutableList<SeatModel>> {
        return mAPI.getEventSectorSeats(eventId, sectorId, limit)
    }

    override fun getEventSectorPoints(
        eventId: Int,
        sectorId: Int,
        limit: Int
    ): Observable<MutableList<PointModel>> {
        return mAPI.getEventSectorPoints(eventId, sectorId, limit)
    }

    override fun getEventSectorImage(
        eventId: Int,
        sectorId: Int
    ): Observable<MutableList<SectorImageModel>> {
        return mAPI.getEventSectorImage(eventId, sectorId)
    }

    override fun getEventSectorSvg(
        eventId: Int,
        sectorId: Int
    ): Observable<MutableList<SectorSvgModel>> {
        return mAPI.getEventSectorSvg(eventId, sectorId)
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
}