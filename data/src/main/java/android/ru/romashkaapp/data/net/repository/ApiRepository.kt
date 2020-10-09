package android.ru.romashkaapp.data.net.repository

import android.ru.romashkaapp.data.net.api.API
import android.ru.romashkaapp.models.*
import android.ru.romashkaapp.repository.Repository
import io.reactivex.Observable
import okhttp3.ResponseBody


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

    override fun getUnit(id: Int): Observable<UnitModel> {
        return mAPI.getUnit(id)
    }

    override fun getHalls(): Observable<MutableList<HallModel>> {
        return mAPI.getHalls()
    }

    override fun getHall(id: Int): Observable<HallModel> {
        return mAPI.getHall(id)
    }

    override fun getCities(): Observable<MutableList<CityModel>> {
        return mAPI.getCities()
    }

    override fun getCity(id: Int): Observable<CityModel> {
        return mAPI.getCity(id)
    }

    override fun getCategories(): Observable<MutableList<CategoryModel>> {
        return mAPI.getCategories()
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

    override fun getNoms(): Observable<MutableList<NomModel>> {
        return mAPI.getNoms()
    }

    override fun getNom(nomId: Int): Observable<NomModel> {
        return mAPI.getNom(nomId)
    }
}