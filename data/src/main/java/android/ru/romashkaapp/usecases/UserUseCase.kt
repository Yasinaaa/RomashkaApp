package android.ru.romashkaapp.usecases

import android.annotation.SuppressLint
import android.ru.romashkaapp.data.net.repository.ApiRepository
import android.ru.romashkaapp.models.UserModel
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

    fun <S> getUser(useCaseDisposable: S) where S : Observer<in UserModel>?, S : Disposable {
         mRepository.getUser(5)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }

    fun <S> editUser(userId: Int, user: UserModel, useCaseDisposable: S) where S : Observer<ResponseBody>?, S : Disposable {
        mRepository.editUser(userId, user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }
}