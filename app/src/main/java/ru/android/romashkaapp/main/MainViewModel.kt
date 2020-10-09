package ru.android.romashkaapp.main

import android.ru.romashkaapp.data.net.api.API
import android.ru.romashkaapp.data.net.repository.ApiRepository
import android.ru.romashkaapp.models.*
import android.ru.romashkaapp.usecases.DictionaryUseCase
import android.ru.romashkaapp.usecases.UserUseCase
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.android.romashkaapp.BaseSubscriber
import ru.android.romashkaapp.BuildConfig
import java.util.concurrent.TimeUnit

/**
 * Created by yasina on 01.10.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
class MainViewModel: ViewModel() {

    private var usecase: UserUseCase? = null
    private var dictionaryUseCase: DictionaryUseCase? = null
    val picture = MutableLiveData<String>()

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
//        usecase = UserUseCase(repository)
//        usecase!!.getUser(UserSubscriber())
//        usecase!!.getUsers(AllUsersSubscriber())

        dictionaryUseCase = DictionaryUseCase(repository)
        dictionaryUseCase!!.getCategories(CategoriesSubscriber())
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

            var id = response.id
            var user = UserModel()
            user.photo = "R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw=="

            usecase!!.editUser(id, user, PatchUserSubscriber())
        }
    }

    private inner class PatchUserSubscriber(): BaseSubscriber<ResponseBody>() {
        override fun onComplete() {
            super.onComplete()
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: ResponseBody) {
            super.onNext(response)
            Log.d("ffd", "ss")
            picture.value = "R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw=="

        }
    }

    private inner class AllUsersSubscriber(): BaseSubscriber<MutableList<UserModel>>() {
        override fun onComplete() {
            super.onComplete()
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<UserModel>) {
            super.onNext(response)
            Log.d("ffd", "fd")
        }
    }

    private inner class CategoriesSubscriber(): BaseSubscriber<MutableList<CategoryModel>>() {
        override fun onComplete() {
            super.onComplete()
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<CategoryModel>) {
            super.onNext(response)
            Log.d("ffd", "CategoriesSubscriber")

            dictionaryUseCase!!.getCategory(response[0].id, CategorySubscriber())
        }
    }

    private inner class CategorySubscriber(): BaseSubscriber<CategoryModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: CategoryModel) {
            super.onNext(response)
            Log.d("ffd", "CategorySubscriber")

            dictionaryUseCase!!.getActions(ActionsSubscriber())
        }
    }

    private inner class ActionsSubscriber(): BaseSubscriber<MutableList<ActionModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<ActionModel>) {
            super.onNext(response)
            Log.d("ffd", "ActionsSubscriber")

            dictionaryUseCase!!.getAction(response[0].id, ActionSubscriber())
        }
    }

    private inner class ActionSubscriber: BaseSubscriber<ActionModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: ActionModel) {
            super.onNext(response)
            Log.d("ffd", "ActionSubscriber")

            dictionaryUseCase!!.getNoms(NomsSubscriber())
        }
    }

    private inner class NomsSubscriber(): BaseSubscriber<MutableList<NomModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<NomModel>) {
            super.onNext(response)
            Log.d("ffd", "NomsSubscriber")

            dictionaryUseCase!!.getNom(response[0].id, NomSubscriber())
        }
    }

    private inner class NomSubscriber: BaseSubscriber<NomModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: NomModel) {
            super.onNext(response)
            Log.d("ffd", "CitiesSubscriber")

            dictionaryUseCase!!.getCities(CitiesSubscriber())
        }
    }

    private inner class CitiesSubscriber(): BaseSubscriber<MutableList<CityModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<CityModel>) {
            super.onNext(response)
            Log.d("ffd", "CitySubscriber")

            dictionaryUseCase!!.getCity(response[0].id, CitySubscriber())
        }
    }

    private inner class CitySubscriber: BaseSubscriber<CityModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: CityModel) {
            super.onNext(response)
            Log.d("ffd", "CitySubscriber")

            dictionaryUseCase!!.getUnits(UnitsSubscriber())
        }
    }

    private inner class UnitsSubscriber(): BaseSubscriber<MutableList<UnitModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<UnitModel>) {
            super.onNext(response)
            Log.d("ffd", "UnitSubscriber")

            dictionaryUseCase!!.getUnit(response[0].id, UnitSubscriber())
        }
    }

    private inner class UnitSubscriber: BaseSubscriber<UnitModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: UnitModel) {
            super.onNext(response)
            Log.d("ffd", "UnitSubscriber")

            dictionaryUseCase!!.getHalls(HallsSubscriber())
        }
    }

    private inner class HallsSubscriber(): BaseSubscriber<MutableList<HallModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<HallModel>) {
            super.onNext(response)
            Log.d("ffd", "UnitSubscriber")

            dictionaryUseCase!!.getHall(response[0].id, HallSubscriber())
        }
    }

    private inner class HallSubscriber: BaseSubscriber<HallModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: HallModel) {
            super.onNext(response)
            Log.d("ffd", "HallSubscriber")

//            dictionaryUseCase!!.getNoms(CategorySubscriber())
        }
    }
}