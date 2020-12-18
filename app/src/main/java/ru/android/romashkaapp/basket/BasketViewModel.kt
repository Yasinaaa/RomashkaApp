package ru.android.romashkaapp.basket

import android.app.Application
import android.ru.romashkaapp.models.CartModel
import android.ru.romashkaapp.models.OrderModel
import android.ru.romashkaapp.usecases.OrderUseCase
import android.view.View
import androidx.lifecycle.MutableLiveData
import ru.android.romashkaapp.BaseSubscriber
import ru.android.romashkaapp.StartActivity
import ru.android.romashkaapp.base.BaseViewModel
import ru.android.romashkaapp.utils.Utils

/**
 * Created by yasina on 05.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */

public interface ItemClickListener{
    fun click(item: CartModel)
}

class BasketViewModel(application: Application) : BaseViewModel(application), View.OnClickListener, ItemClickListener {

    private var currentOrderId: Int = 0
    private var orderUseCase: OrderUseCase? = null
    val list: MutableLiveData<MutableList<CartModel>> = MutableLiveData()

    init {
        orderUseCase = OrderUseCase(StartActivity.REPOSITORY, Utils.getAccessToken(application)!!)
    }

    fun setOrderId(orderId: Int){
        currentOrderId = orderId
        orderUseCase!!.getOrder(currentOrderId, OrderSubscriber())
    }

    private inner class OrderSubscriber: BaseSubscriber<OrderModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: OrderModel) {
            super.onNext(response)
//            cart.value = response
            orderUseCase!!.getUserOrderCarts(response.id, OrderCartsSubscriber())
        }
    }

    private inner class OrderCartsSubscriber: BaseSubscriber<MutableList<CartModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<CartModel>) {
            super.onNext(response)
            list.value = response
        }
    }

    override fun onClick(view: View?) {
        TODO("Not yet implemented")
    }

    fun getListener(): ItemClickListener {
        return this
    }

    override fun click(item: CartModel) {
        TODO("Not yet implemented")
    }
}