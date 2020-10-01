package ru.android.romashkaapp

import android.ru.romashkaapp.data.net.api.API
import android.ru.romashkaapp.data.net.repository.ApiRepository
import android.ru.romashkaapp.models.UserModel
import android.ru.romashkaapp.usecases.UserUseCase
import android.util.Log
import androidx.lifecycle.ViewModel
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by yasina on 01.10.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
class MainViewModel: ViewModel() {

    private var usecase: UserUseCase? = null

    fun api(): API {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor { chain ->
                val request = chain.request()
                var response = chain.proceed(request)
                var tryCount = 0
                while (!response.isSuccessful && tryCount < 3) {
                    Log.d("intercept", "Request is not successful - $tryCount")
                    tryCount++
                    response = chain.proceed(request)
                }
                response
            }.connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
        return Retrofit.Builder()
            .baseUrl("https://private-a905e4-artemshvedenko.apiary-mock.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(client)
            .build().create(API::class.java)
    }

    init{
        val repository = ApiRepository(api())
        usecase = UserUseCase(repository)
        usecase!!.getUser(UserSubscriber())
    }

    private inner class UserSubscriber(): BaseSubscriber<UserModel>() {
        override fun onComplete() {
            super.onComplete()
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: UserModel) {
            super.onNext(response)
            Log.d("ffd", response.email)
        }
    }
}