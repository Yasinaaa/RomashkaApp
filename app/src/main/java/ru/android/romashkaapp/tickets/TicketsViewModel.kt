package ru.android.romashkaapp.tickets

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
import ru.android.romashkaapp.tickets.adapters.TicketsAdapter
import ru.android.romashkaapp.utils.Utils

/*
 * Created by yasina on 23.12.2020
*/
public interface ItemClickListener{
    fun setYear(year: String)
    fun onGooglePayClick(item: TicketsAdapter.Ticket)
    fun onShareClick(item: TicketsAdapter.Ticket)
}

class TicketsViewModel(application: Application) : BaseViewModel(application), View.OnClickListener, ItemClickListener {

    private var currentOrderId: Int = 0
    private var orderUseCase: OrderUseCase? = null
    private var eventUseCase: EventsUseCase? = null
    private var dictionaryUseCase: DictionaryUseCase? = null
    val list: MutableLiveData<MutableList<TicketsAdapter.Ticket?>> = MutableLiveData()
    val year: MutableLiveData<String> = MutableLiveData()

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

        orderUseCase!!.getAllOrders(OrdersSubscriber())
    }

    private inner class OrdersSubscriber: BaseSubscriber<MutableList<OrderModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<OrderModel>) {
            super.onNext(response)


            for (i in response){//todo for several orders
                if (i.status_id == 2){
                    currentOrderId = i.id
                    orderUseCase!!.getUserOrderCarts(i.id, OrderCartsSubscriber())
                    break
                }
            }
        }
    }

    fun getEvents(carts: MutableList<CartModel>) {
        dictionaryUseCase!!.getNoms(perPage = 1000, page = 2, NomsSubscriber(carts)) //todo right way
    }

    private inner class NomsSubscriber(l: MutableList<CartModel>) :
        BaseSubscriber<MutableList<NomModel>>() {

        var carts: MutableList<CartModel> = l

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<NomModel>) {
            super.onNext(response)
            Log.d("ffd", "NomsSubscriber")
            eventUseCase!!.getEvents(page = null, perPage = 100, EventsSubscriber(response, carts))
        }
    }

    private inner class EventsSubscriber(n: MutableList<NomModel>, c: MutableList<CartModel>) :
        BaseSubscriber<MutableList<EventModel>>() {

        var noms: MutableList<NomModel> = n
        var carts: MutableList<CartModel> = c

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<EventModel>) {
            super.onNext(response)
            Log.d("ffd", "EventsSubscriber")

            val matches: MutableList<TicketsAdapter.Ticket?> = mutableListOf()
            carts.forEach { c ->
                val match = TicketsAdapter.Ticket()
                response.forEach { event ->
                    if(event.id == c.event_id) {
                        getBarcode(c.id)
                        match.cart = c
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
                    matches.add(match)
                }
            }
            list.value = matches
        }
    }

    private fun getBarcode(cartId: Int){
        orderUseCase!!.getBarcode(currentOrderId, cartId, BarcodeSubscriber(cartId))
    }

    private inner class BarcodeSubscriber(var cartId: Int): BaseSubscriber<BarcodeModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: BarcodeModel) {
            super.onNext(response)
            var l = list.value
            l!!.forEach {
                if(it != null) {
                    if (it.cart.id == cartId) {
                        it.qrCode = response.barcode
                        return@forEach
                    }
                }
            }.also {
                list.value = l
            }
        }
    }

    private inner class AreasSubscriber(var cart: TicketsAdapter.Ticket) : BaseSubscriber<MutableList<AreaModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<AreaModel>) {
            super.onNext(response)
            if(response.size == 1){

                var l = list.value
                l!!.forEach {
                    if(it != null) {
                        if (it.event.id == cart.event.id) {
                            it.location = response[0].name
                            return@forEach
                        }
                    }
                }.also {
                    list.value = l
                }
            }
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

//    private fun getTickets(){
//        orderUseCase!!.getBarcodes(currentOrderId, BarcodesSubscriber())
//    }
//
//    private inner class BarcodesSubscriber: BaseSubscriber<MutableList<BarcodeModel>>() {
//
//        override fun onError(e: Throwable) {
//            super.onError(e)
//        }
//
//        override fun onNext(response: MutableList<BarcodeModel>) {
//            super.onNext(response)
//            getTickets()
//        }
//    }

    override fun onClick(view: View?) {
        TODO("Not yet implemented")
    }

    fun getListener(): ItemClickListener {
        return this
    }

    override fun setYear(y: String) {
        year.value = y
    }

    override fun onGooglePayClick(item: TicketsAdapter.Ticket) {
        TODO("Not yet implemented")
    }

    override fun onShareClick(item: TicketsAdapter.Ticket) {
        TODO("Not yet implemented")
    }
}