package ru.android.romashkaapp.basket

import android.app.Application
import android.ru.romashkaapp.models.*
import android.ru.romashkaapp.usecases.DictionaryUseCase
import android.ru.romashkaapp.usecases.EventsUseCase
import android.ru.romashkaapp.usecases.OrderUseCase
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import ru.android.romashkaapp.BaseSubscriber
import ru.android.romashkaapp.StartActivity
import ru.android.romashkaapp.base.BaseViewModel
import ru.android.romashkaapp.basket.adapters.CartAdapter
import ru.android.romashkaapp.utils.Utils

/**
 * Created by yasina on 05.11.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */

public interface ItemClickListener{
    fun click(item: CartAdapter.OrderEventWithSeats)
}

class BasketViewModel(application: Application) : BaseViewModel(application), View.OnClickListener, ItemClickListener {

    private var currentOrderId: Int = 0
    private var orderUseCase: OrderUseCase? = null
    private var eventUseCase: EventsUseCase? = null
    private var dictionaryUseCase: DictionaryUseCase? = null
    val list: MutableLiveData<MutableList<CartAdapter.OrderEventWithSeats>> = MutableLiveData()

    init {
        orderUseCase = OrderUseCase(StartActivity.REPOSITORY, Utils.getAccessToken(application)!!)
        dictionaryUseCase = DictionaryUseCase(
            StartActivity.REPOSITORY,
            Utils.getAccessToken(application.applicationContext)!!
        )
        eventUseCase = EventsUseCase(
            StartActivity.REPOSITORY,
            Utils.getAccessToken(application.applicationContext)!!
        )
    }

    fun setOrderId(orderId: Int){
        currentOrderId = orderId
        orderUseCase!!.getOrder(currentOrderId, OrderSubscriber())
    }

    fun getEvents(carts: MutableList<CartModel>) {
        dictionaryUseCase!!.getNoms(perPage = 1000, page = 2, NomsSubscriber(carts)) //todo right way
    }

    private inner class NomsSubscriber : BaseSubscriber<MutableList<NomModel>> {

        var carts: MutableList<CartModel>

        constructor(l: MutableList<CartModel>){
            carts = l
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<NomModel>) {
            super.onNext(response)
            Log.d("ffd", "NomsSubscriber")
            eventUseCase!!.getEvents(page = null, perPage = 100, EventsSubscriber(response, carts))
        }
    }

    private inner class EventsSubscriber : BaseSubscriber<MutableList<EventModel>> {

        var noms: MutableList<NomModel> = mutableListOf()
        var carts: MutableList<CartModel>

        constructor(n: MutableList<NomModel>, c: MutableList<CartModel>) {
            noms = n
            carts = c
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<EventModel>) {
            super.onNext(response)
            Log.d("ffd", "EventsSubscriber")

            val matches: MutableList<CartAdapter.OrderEventWithSeats> = mutableListOf()
            carts.forEach { c ->
                var newMatch = true

                matches.forEach { m ->
                    if(m.event != null) {
                        if (m.event!!.id == c.event_id) {
                            newMatch = false
                            m.seats.add(c)
                            return@forEach
                        }
                    }
                }

                if(newMatch){
                    val match = CartAdapter.OrderEventWithSeats()
                    response.forEach { event ->
                        if(event.id == c.event_id) {
                            match.event = event
                            for (n in noms) {
                                //todo get all
                                if (event.nom_id == n.id) {
                                    match.nomTitle = n.name
                                    eventUseCase!!.getEventAreas(eventId = event.id, AreasSubscriber(match))
                                    return@forEach
                                }
                            }
                        }
                    }.also {
                        match.seats.add(c)
                        matches.add(match)
                    }
                }
            }
            list.value = matches
        }
    }

    private inner class AreasSubscriber: BaseSubscriber<MutableList<AreaModel>> {

        var cart: CartAdapter.OrderEventWithSeats

        constructor(cart: CartAdapter.OrderEventWithSeats){
            this.cart = cart
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<AreaModel>) {
            super.onNext(response)
            if(response.size == 1){

                var l = list.value
                l!!.forEach {
                    if(it.event!!.id == cart.event!!.id){
                        it.location = response[0].name
                        return@forEach
                    }
                }.also {
                    list.value = l
                }
            }
        }
    }

    private inner class OrderSubscriber: BaseSubscriber<OrderModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: OrderModel) {
            super.onNext(response)
            orderUseCase!!.getUserOrderCarts(response.id, OrderCartsSubscriber())
        }
    }

    private inner class OrderCartsSubscriber: BaseSubscriber<MutableList<CartModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<CartModel>) {
            super.onNext(response)
            getEvents(response)
        }
    }

    override fun onClick(view: View?) {
        TODO("Not yet implemented")
    }

    fun getListener(): ItemClickListener {
        return this
    }

    override fun click(item: CartAdapter.OrderEventWithSeats) {
        TODO("Not yet implemented")
    }
}