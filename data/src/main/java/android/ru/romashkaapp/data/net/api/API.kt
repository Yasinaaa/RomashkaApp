package android.ru.romashkaapp.data.net.api

import android.ru.romashkaapp.models.*
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by yasina on 25.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
interface API {

    ///api/v1
    @GET("/users/{user_id}")
    fun getUser(@Path("user_id") userId: Int): Observable<UserModel>

    @PATCH("/api/v1/users/{user_id}")
    fun editUser(@Path("user_id") userId: Int, @Body user: UserModel): Observable<ResponseBody>

    @GET("/api/v1/events")
    fun getEvents(): Observable<MutableList<EventModel>>

    @GET("/api/v1/events/{event_id}")
    fun getEvent(@Path("event_id") eventId: Int): Observable<EventModel>

    @GET("/api/v1/events/{event_id}/sectors/{sector_id}")
    fun getSector(@Path("event_id") eventId: Int, @Path("sector_id") sectorId: Int): Observable<SectorModel>

    @GET("/api/v1/events/{event_id}/{sector_id}/places")
    fun getSectorPlaces(@Path("event_id") eventId: Int, @Path("sector_id") sectorId: Int): Observable<SeatModel>

    @GET("/api/v1/events/{event_id}/{sector_id}/plan")
    fun getSectorSvg(@Path("event_id") eventId: Int, @Path("sector_id") sectorId: Int): Observable<SectorSvgModel>

    @GET("/api/v1/users/{user_id}/orders")
    fun getUserOrders(@Path("user_id") userId: Int): Observable<MutableList<OrderModel>>

    @GET("/api/v1/users/{user_id}/orders/{order_id}")
    fun getUserOrder(@Path("user_id") userId: Int, @Path("order_id") orderId: Int): Observable<OrderModel>

    @POST("/api/v1/users/{user_id}/orders")
    fun createOrder(@Path("user_id") userId: Int, @Body order: OrderModel): Observable<OrderModel> //check

    @POST("/api/v1/users/{user_id}/orders/{order_id}/carts")
    fun addSeatToOrder(@Path("user_id") userId: Int, @Path("order_id") orderId: Int, @Query("sid") sid: String): Observable<CartModel>

    @DELETE("/api/v1/users/{user_id}/orders/{order_id}/carts/{cart_id}")
    fun deleteSeatFromCart(@Path("user_id") userId: Int, @Path("order_id") orderId: Int, @Path("cart_id") cartId: Int): Observable<ResponseBody>

    @GET("/pay/pay/{id}")
    fun payOrder(@Path("id") orderId: Int): Observable<ResponseBody>

    @GET("/api/v1/units")
    fun getUnits(): Observable<MutableList<UnitModel>>

    @GET("/api/v1/halls")
    fun getHalls(): Observable<MutableList<HallModel>>

    @GET("/api/v1/cities")
    fun getCities(): Observable<MutableList<CityModel>>

    @GET("/api/v1/categories")
    fun getCategories(): Observable<MutableList<CategoryModel>>

    @GET("/api/v1/categories")
    fun getActions(): Observable<MutableList<ActionModel>>

    @GET("/api/v1/noms")
    fun getNoms(): Observable<MutableList<NomModel>>
}