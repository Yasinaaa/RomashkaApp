package android.ru.romashkaapp.usecases

import android.annotation.SuppressLint
import android.ru.romashkaapp.data.net.repository.ApiRepository
import android.ru.romashkaapp.models.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import javax.inject.Inject


/**
 * Created by yasina on 01.10.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
@SuppressLint("CheckResult")
class UserUseCase(
    private val mRepository: ApiRepository
): ApiUseCase () {

    fun <S> getAppToken(clientId: String?,
                        clientSecret: String?,
                        grantType: String?, useCaseDisposable: S) where S : Observer<in AppTokenResponse>?, S : Disposable {
        mRepository.getAppToken(AppToken(clientId, clientSecret, grantType))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getClientToken(clientId: String?,
                        clientSecret: String?,
                        grantType: String?, username: String?, password: String?, useCaseDisposable: S) where S : Observer<in ClientTokenResponse>?, S : Disposable {
        mRepository.getClientToken(ClientToken(clientId, clientSecret, grantType, username, password))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getUsers(accessToken: String, useCaseDisposable: S) where S : Observer<in MutableList<UserModel>>?, S : Disposable {
        mRepository.getUsers(accessToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> getUser(useCaseDisposable: S) where S : Observer<in UserModel>?, S : Disposable {
         mRepository.getUser(5)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> editUser(userId: Long, user: UserModel, useCaseDisposable: S) where S : Observer<ResponseBody>?, S : Disposable {
        mRepository.editUser(userId, user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }
}