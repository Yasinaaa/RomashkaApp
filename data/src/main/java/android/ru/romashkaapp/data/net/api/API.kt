package android.ru.romashkaapp.data.net.api

import android.ru.romashkaapp.models.EventModel
import android.ru.romashkaapp.models.UserModel
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

/**
 * Created by yasina on 25.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
interface API {

    //@Headers()
    @GET("/api/v1/users/{user_id}")
    fun getUser(@Path("user_id") userId: Int): Observable<UserModel>

    @PATCH("/api/v1/users/{user_id}")
    fun editUser(@Path("user_id") userId: Int, @Body user: UserModel): Observable<ResponseBody>

    @GET("/api/v1/events")
    fun getEvents(): Observable<MutableList<EventModel>>

    @GET("/api/v1/events/{event_id}")
    fun getEvent(@Path("event_id") eventId: Int): Observable<EventModel>

    @GET("/api/v1/events/{event_id}/sectors/{sector_id}")
    fun getSector(@Path("event_id") eventId: Int): Observable<EventModel>

    @GET("/api/v1/events/{event_id}/sectors/{sector_id}") //доделать
    fun getPlacesSector(@Path("event_id") eventId: Int): Observable<EventModel>
}