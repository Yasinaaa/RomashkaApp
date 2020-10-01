package android.ru.romashkaapp.data.net.repository

import android.ru.romashkaapp.data.net.api.API
import android.ru.romashkaapp.models.*
import android.ru.romashkaapp.repository.Repository
import io.reactivex.Observable
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by yasina on 26.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
//@Singleton
class ApiRepository constructor(private val mAPI: API) : Repository {
    
    override fun getUser(userId: Int): Observable<UserModel> {
        return mAPI.getUser(userId)
    }

    override fun editUser(userId: Int, user: UserModel): Observable<ResponseBody> {
        return mAPI.editUser(userId, user)
    }

    override fun getEvents(): Observable<MutableList<EventModel>> {
        return mAPI.getEvents()
    }

    override fun getEvent(eventId: Int): Observable<EventModel> {
        return mAPI.getEvent(eventId)
    }

    override fun getSector(eventId: Int, sectorId: Int): Observable<SectorModel> {
        return mAPI.getSector(eventId, sectorId)
    }

    override fun getSectorPlaces(eventId: Int, sectorId: Int): Observable<SeatModel> {
        return mAPI.getSectorPlaces(eventId, sectorId)
    }

    override fun getSectorSvg(eventId: Int, sectorId: Int): Observable<SectorSvgModel> {
        return mAPI.getSectorSvg(eventId, sectorId)
    }

    override fun getUserOrders(userId: Int): Observable<MutableList<OrderModel>> {
        return mAPI.getUserOrders(userId)
    }

    override fun getUserOrder(userId: Int, orderId: Int): Observable<OrderModel> {
        return mAPI.getUserOrder(userId, orderId)
    }

    override fun createOrder(userId: Int, order: OrderModel): Observable<OrderModel> {
        return mAPI.createOrder(userId, order)
    }

    override fun addSeatToOrder(userId: Int, orderId: Int, sid: String): Observable<CartModel> {
        return mAPI.addSeatToOrder(userId, orderId, sid)
    }

    override fun deleteSeatFromCart(
        userId: Int,
        orderId: Int,
        cartId: Int
    ): Observable<ResponseBody> {
        return mAPI.deleteSeatFromCart(userId, orderId, cartId)
    }

    override fun payOrder(orderId: Int): Observable<ResponseBody> {
        return mAPI.payOrder(orderId)
    }

    override fun getUnits(): Observable<MutableList<UnitModel>> {
        return mAPI.getUnits()
    }

    override fun getHalls(): Observable<MutableList<HallModel>> {
        return mAPI.getHalls()
    }

    override fun getCities(): Observable<MutableList<CityModel>> {
        return mAPI.getCities()
    }

    override fun getCategories(): Observable<MutableList<CategoryModel>> {
        return mAPI.getCategories()
    }

    override fun getActions(): Observable<MutableList<ActionModel>> {
        return mAPI.getActions()
    }

    override fun getNoms(): Observable<MutableList<NomModel>> {
        return mAPI.getNoms()
    }
}