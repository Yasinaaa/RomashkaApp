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
    @Headers("Content-Type: application/json")
    @POST("/yasina/oauth2/token")
    fun getAppToken(@Body appToken: AppToken): Observable<AppTokenResponse>

    @Headers("Content-Type: application/json")
    @POST("/oauth2/token")
    fun getClientToken(@Body clientToken: ClientToken): Observable<ClientTokenResponse>

    @GET("/v1/users")
    fun getUsers(@Query("accessToken") accessToken: String): Observable<MutableList<UserModel>>

    @GET("/users/{user_id}")
    fun getUser(@Path("user_id") userId: Long): Observable<UserModel>

    @PATCH("/users/{user_id}")
    fun editUser(@Path("user_id") userId: Long, @Body user: UserModel): Observable<ResponseBody>

    @GET("/yasina/v1/events")
    fun getEvents(@Query("accessToken") accessToken: String, @Query("page") page: Int?, @Query("per-page") perPage: Int?): Observable<MutableList<EventModel>>

    @GET("/events")
    fun getEvents(@Query("last_gt") last: String?, @Query("limit") limit: String?,
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

    @GET("/yasina/v1/events/{event_id}")
    fun getEvent(@Path("event_id") eventId: Int, @Query("accessToken") accessToken: String): Observable<EventModel>

    //todo
    @GET("/events/{event_id}/subscriptions")
    fun getEventSubscriptions(@Path("event_id") eventId: Int): Observable<MutableList<EventModel>>

    @GET("/events/{event_id}/area/sectors/{sector_id}")
    fun getEventSector(@Path("event_id") eventId: Int, @Path("sector_id") sectorId: Int,
                       @Query("last_gt") last: String?,
                       @Query("last_seats_gt") lastSeatsGt: String?,
                       @Query("last_area_gt") lastAreaGt: String?): Observable<SectorModel>

    @GET("/events/{event_id}/area/sectors/{sector_id}/zones")
    fun getEventSectorZones(@Path("event_id") eventId: Int, @Path("sector_id") sectorId: Int,
                       @Query("limit") limit: Int): Observable<MutableList<ZoneModel>>

    @GET("/events/{event_id}/area/sectors/{sector_id}/seats")
    fun getEventSectorSeats(@Path("event_id") eventId: Int, @Path("sector_id") sectorId: Int,
                            @Query("limit") limit: Int): Observable<MutableList<SeatModel>>

    @GET("/events/{event_id}/area/sectors/{sector_id}/points")
    fun getEventSectorPoints(@Path("event_id") eventId: Int, @Path("sector_id") sectorId: Int,
                            @Query("limit") limit: Int): Observable<MutableList<PointModel>>

    @GET("/events/{event_id}/area/sectors/{sector_id}/image")
    fun getEventSectorImage(@Path("event_id") eventId: Int, @Path("sector_id") sectorId: Int): Observable<SectorImageModel>

    @GET("/events/{event_id}/area/sectors/{sector_id}/svg")
    fun getEventSectorSvg(@Path("event_id") eventId: Int, @Path("sector_id") sectorId: Int): Observable<SectorSvgModel>

    @GET("/orders")
    fun getUserOrders(@Query("status") status: Int): Observable<MutableList<OrderModel>>
//
//    @GET("/api/v1/users/{user_id}/orders/{order_id}")
//    fun getUserOrder(@Path("user_id") userId: Int, @Path("order_id") orderId: Int): Observable<OrderModel>
//
//    @POST("/api/v1/users/{user_id}/orders")
//    fun createOrder(@Path("user_id") userId: Int, @Body order: OrderModel): Observable<OrderModel> //check
//
//    @POST("/api/v1/users/{user_id}/orders/{order_id}/carts")
//    fun addSeatToOrder(@Path("user_id") userId: Int, @Path("order_id") orderId: Int, @Query("sid") sid: String): Observable<CartModel>
//
//    @DELETE("/api/v1/users/{user_id}/orders/{order_id}/carts/{cart_id}")
//    fun deleteSeatFromCart(@Path("user_id") userId: Int, @Path("order_id") orderId: Int, @Path("cart_id") cartId: Int): Observable<ResponseBody>
//
//    @GET("/pay/pay/{id}")
//    fun payOrder(@Path("id") orderId: Int): Observable<ResponseBody>

    @GET("/units")
    fun getUnits(@Query("last_gt") last: String?, @Query("limit") limit: String?): Observable<MutableList<UnitModel>>

    @GET("/units/{unit_id}")
    fun getUnit(@Path("unit_id") id: Int): Observable<UnitModel>

    @GET("/halls")
    fun getHalls(@Query("last_gt") last: String?, @Query("limit") limit: String?): Observable<MutableList<HallModel>>

    @GET("/halls/{hall_id}")
    fun getHall(@Path("hall_id") id: Int): Observable<HallModel>

    @GET("/cities")
    fun getCities(@Query("last_gt") last: String?, @Query("limit") limit: String?): Observable<MutableList<CityModel>>

    @GET("/cities/{city_id}")
    fun getCity(@Path("city_id") id: Int): Observable<CityModel>

    @GET("/v1/categories")
    fun getCategories(@Query("accessToken") accessToken: String, @Query("last_gt") last: String?, @Query("limit") limit: String?): Observable<MutableList<CategoryModel>>

    @GET("/categories/{category_id}")
    fun getCategory(@Path("category_id") categoryId: Int): Observable<CategoryModel>

    @GET("/yasina/v1/noms")
    fun getNoms(@Query("accessToken") accessToken: String, @Query("last_gt") last: String?, @Query("limit") limit: String?): Observable<MutableList<NomModel>>

    @GET("/noms/{nom_id}")
    fun getNom(@Path("nom_id") nomId: Int): Observable<NomModel>

    @GET("/v1/actions")
    fun getActions(@Query("accessToken") accessToken: String): Observable<MutableList<ActionModel>>

    @GET("/actions/{action_id}")
    fun getAction(@Path("action_id") actionId: Int): Observable<ActionModel>

    @GET("/services")
    fun getServices(@Query("last_gt") last: String?, @Query("limit") limit: String?,
                    @Query("active") active: Boolean?,
                    @Query("unit_id") unitId: Int?): Observable<MutableList<ServiceModel>>

    @GET("/services/{service_id}")
    fun getService(@Path("service_id") serviceId: Int): Observable<ServiceModel>
}