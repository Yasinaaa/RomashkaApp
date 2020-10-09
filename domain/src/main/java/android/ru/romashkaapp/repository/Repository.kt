package android.ru.romashkaapp.repository

import okhttp3.ResponseBody
import io.reactivex.Observable
import android.ru.romashkaapp.models.*

/**
 * Created by yasina on 29.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
interface Repository {

    fun getUsers(): Observable<MutableList<UserModel>>
    fun getUser(userId: Long): Observable<UserModel>
    fun editUser(userId: Long, user: UserModel): Observable<ResponseBody>
    fun getEvents(): Observable<MutableList<EventModel>>
    fun getEvent(eventId: Int): Observable<EventModel>
    fun getSector(eventId: Int,  sectorId: Int): Observable<SectorModel>
    fun getSectorPlaces(eventId: Int,  sectorId: Int): Observable<SeatModel>
    fun getSectorSvg(eventId: Int,  sectorId: Int): Observable<SectorSvgModel>
    fun getUserOrders(userId: Int): Observable<MutableList<OrderModel>>
    fun getUserOrder(userId: Int, orderId: Int): Observable<OrderModel>
    fun createOrder(userId: Int, order: OrderModel): Observable<OrderModel>
    fun addSeatToOrder(userId: Int, orderId: Int, sid: String): Observable<CartModel>
    fun deleteSeatFromCart(userId: Int, orderId: Int, cartId: Int): Observable<ResponseBody>
    fun payOrder(orderId: Int): Observable<ResponseBody>
    fun getUnits(): Observable<MutableList<UnitModel>>
    fun getUnit(id: Int): Observable<UnitModel>
    fun getHalls(): Observable<MutableList<HallModel>>
    fun getHall(id: Int): Observable<HallModel>
    fun getCities(): Observable<MutableList<CityModel>>
    fun getCity(id: Int): Observable<CityModel>
    fun getCategories(): Observable<MutableList<CategoryModel>>
    fun getCategory(categoryId: Int): Observable<CategoryModel>
    fun getActions(): Observable<MutableList<ActionModel>>
    fun getAction(actionId: Int): Observable<ActionModel>
    fun getNoms(): Observable<MutableList<NomModel>>
    fun getNom(nomId: Int): Observable<NomModel>
}