package android.ru.romashkaapp.usecases

import android.ru.romashkaapp.data.net.repository.ApiRepository
import android.ru.romashkaapp.models.UserModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


/**
 * Created by yasina on 01.10.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
class UserUseCase(
    private val mRepository: ApiRepository
): ApiUseCase () {

    private var disposable = Disposables.empty()

    public fun <S> getUser(useCaseDisposable: S) where S : Observer<in UserModel>?, S : Disposable {
        disposable = mRepository.getUser(5)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(useCaseDisposable)
    }
}