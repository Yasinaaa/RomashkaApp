package android.ru.romashkaapp.data.net.api

import android.ru.romashkaapp.models.*
import android.ru.romashkaapp.models.request.UserRequestModel
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by yasina on 25.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
interface API {
    
    @POST("/yasina/oauth2/token")
    fun getAppToken(@Body appToken: AppToken): Observable<AppTokenResponse>

    @POST("/yasina/oauth2/token")
    fun getClientToken(@Body clientToken: ClientToken): Observable<ClientTokenResponse>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("/yasina/v1/users")
    fun addUser(@Body user: UserRequestModel, @Header("Authorization") accessToken: String): Observable<ResponseBody>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("/yasina/v1/events")
    fun getEvents(@Header("Authorization") accessToken: String, @Query("page") page: Int?, @Query("per-page") perPage: Int?): Observable<MutableList<EventModel>>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("/events")
    fun getEvents(@Query("last_gt") last: String?,
                  @Query("limit") limit: String?,
                  @Query("active") active: Boolean?,
                  @Query("unit_id") unitId: Int?,
                  @Query("hall_id") hallId: Int?,
                  @Query("nom_id") nomId: Int?,
                  @Query("action_id") actionId: Int?,
                  @Query("category_id") categoryId: Int?,
                  @Query("sdate_gt") sdateGt: String?,
                  @Query("sdate_ls") sdateLs: String?,
                  @Query("edate_gt") edateGt: String?,
                  @Query("sdate_ls") edateLs: String?,
                  @Query("type") type: String?): Observable<MutableList<EventModel>>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("/yasina/v1/events/{event_id}")
    fun getEvent(@Path("event_id") eventId: Int, @Header("Authorization") accessToken: String): Observable<EventModel>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("/yasina/v1/events/{event_id}/areas")
    fun getEventAreas(@Path("event_id") eventId: Int, @Header("Authorization") accessToken: String): Observable<MutableList<AreaModel>>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("/yasina/v1/events/{event_id}/areas/{area_id}/sectors")
    fun getEventArea(@Path("event_id") eventId: Int, @Path("area_id") areaId: Int,
                     @Header("Authorization") accessToken: String): Observable<MutableList<SectorModel>>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("/yasina/v1/events/{event_id}/areas/{area_id}/plan")
    fun getEventAreaPlan(@Path("event_id") eventId: Int, @Path("area_id") areaId: Int,
                         @Query("type") type: String,
                         @Header("Authorization") accessToken: String): Observable<ResponseBody>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("/yasina/v1/events/{event_id}/areas/{area_id}/sectors/{sector_id}/places")
    fun getEventSectorSeats(@Path("event_id") eventId: Int, @Path("sector_id") sectorId: String?,
                            @Path("area_id") areaId: Int, @Query("type") type: String?,
                            @Header("Authorization") accessToken: String): Observable<MutableList<SeatModel>>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("/yasina/v1/events/{event_id}/areas/{area_id}/zones")
    fun getEventSectorZones(@Path("event_id") eventId: Int, @Path("area_id") areaId: Int,
                            @Header("Authorization") accessToken: String): Observable<MutableList<ZoneModel>>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("/yasina/v1/events/{event_id}/areas/{area_id}/zones/places")
    fun getEventZonePlaces(@Path("event_id") eventId: Int, @Path("area_id") areaId: Int,
                           @Header("Authorization") accessToken: String): Observable<MutableList<ZoneWithFreePlacesModel>>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("/yasina/v1/events/{event_id}/areas/{area_id}/sectors/{sector_id}/statuses")
    fun getEventSectorStatuses(@Path("event_id") eventId: Int, @Path("sector_id") sectorId: Int,
                               @Path("area_id") areaId: Int,
                               @Header("Authorization") accessToken: String): Observable<MutableList<StatusModel>>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("/yasina/v1/events/{event_id}/areas/{area_id}/carts")
    fun addToCart(@Path("event_id") eventId: Int, @Path("area_id") areaId: Int,
                  @Body sid: Sid,
                  @Header("Authorization") accessToken: String): Observable<OrderIdModel>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @HTTP(method = "DELETE", path = "/yasina/v1/events/{event_id}/areas/{area_id}/carts", hasBody = true)
    fun deleteSeatFromCart(@Path("event_id") eventId: Int, @Path("area_id") areaId: Int,
                           @Body sid: Sid,
                           @Header("Authorization") accessToken: String): Observable<ResponseBody>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("/yasina/v1/pay/{order_id}")
    fun payOrder(@Path("order_id") orderId: Int, @Header("Authorization")  accessToken: String): Observable<ResponseBody>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("/yasina/v1/orders/{order_id}")
    fun getUserOrder(@Path("order_id") orderId: Int, @Header("Authorization") accessToken: String): Observable<OrderModel>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("/yasina/v1/orders/{order_id}/carts")
    fun getUserOrderCarts(@Path("order_id") orderId: Int, @Header("Authorization") accessToken: String,
                          @Header("Accept-Language") lang: String): Observable<MutableList<CartModel>>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("/yasina/v1/orders")
    fun getAllOrders(@Header("Authorization") accessToken: String): Observable<MutableList<OrderModel>>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("/yasina/v1/halls/{hall_id}")
    fun getHall(@Path("hall_id") id: Int, @Header("Authorization") accessToken: String): Observable<HallModel>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @GET("/yasina/v1/noms")
    fun getNoms(@Header("Authorization") accessToken: String, @Query("per-page") perPage: Int?, @Query("page") page: Int?): Observable<MutableList<NomModel>>

    @Headers("Content-Type: application/pdf")
    @GET("/yasina/v1/orders/{order_id}/tickets")
    fun getTickets(@Path("order_id") orderId: Int, @Header("Authorization") accessToken: String): Observable<ResponseBody>

}