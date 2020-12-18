package ru.android.romashkaapp.sector_seat

import android.app.Application
import android.ru.romashkaapp.models.*
import android.ru.romashkaapp.usecases.DictionaryUseCase
import android.ru.romashkaapp.usecases.EventsUseCase
import android.ru.romashkaapp.usecases.OrderUseCase
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.ResponseBody
import ru.android.romashkaapp.BaseSubscriber
import ru.android.romashkaapp.R
import ru.android.romashkaapp.StartActivity
import ru.android.romashkaapp.adapter.helpers.SwipeRemoveActionListener
import ru.android.romashkaapp.base.BaseViewModel
import ru.android.romashkaapp.basket.BasketFragment
import ru.android.romashkaapp.basket.BasketFragment.Companion.ORDER_ID
import ru.android.romashkaapp.stadium.ItemClickListener
import ru.android.romashkaapp.utils.Utils

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class SectorSeatViewModel(application: Application) : BaseViewModel(application), View.OnClickListener, SwipeRemoveActionListener,
    ItemClickListener {

    companion object{
        val FREE_PLACE = 0
        val BOOKED_SEAT_BY_CURRENT_USER = 1
        val BOOKED_SEAT_BY_OTHER_USER = 2
    }

    val zoomView = MutableLiveData<Boolean?>()

    val list: MutableLiveData<MutableList<CartModel>> = MutableLiveData()

    private val _deletedPosition: MutableLiveData<Int> = MutableLiveData()
    private var previousDeletedItem: CartModel? = null

    private var eventUseCase: EventsUseCase? = null
    private var dictionaryUseCase: DictionaryUseCase? = null
    private var orderUseCase: OrderUseCase? = null
    val seatsCoordinates = MutableLiveData<MutableList<SeatModel>>()
    private var eventId: Int = 0
    private var areaId: Int = 0
    private var currentOrderId: Int = 0
    private var sectorId: String? = null
    val sectorLive = MutableLiveData<String>()
    val sectorPrevLive = MutableLiveData<String?>()
    val sectorNextLive = MutableLiveData<String?>()
    val cart = MutableLiveData<OrderModel>()
    val nextFragment = MutableLiveData<Fragment>()
    var allSectors: MutableList<SectorModel> = mutableListOf()
    val pricesList: MutableLiveData<MutableList<ZoneModel>> = MutableLiveData()

    init {
        eventUseCase = EventsUseCase(StartActivity.REPOSITORY, Utils.getAccessToken(application)!!)
        orderUseCase = OrderUseCase(StartActivity.REPOSITORY, Utils.getAccessToken(application)!!)
        dictionaryUseCase = DictionaryUseCase(StartActivity.REPOSITORY, Utils.getAccessToken(application)!!)
    }

    fun getSeats(eventId: Int, areaId: Int, sectorId: String?){
        this.eventId = eventId
        this.areaId = areaId
        this.sectorId = sectorId
        sectorLive.value = String.format(context.getString(R.string.sector), sectorId)

        eventUseCase!!.getEventSectorSeats(eventId=eventId, areaId = areaId, sectorId = sectorId.toString(), type = null,
                        useCaseDisposable = SectorSeatsSubscriber(areaId))

        eventUseCase!!.getEventSectorZones(eventId = eventId, areaId = areaId,  SectorZonesSubscriber(areaId))
        eventUseCase!!.getEventArea(eventId, areaId, AreasSubscriber())
    }

    private inner class AreasSubscriber: BaseSubscriber<MutableList<SectorModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<SectorModel>) {
            super.onNext(response)
            if(response.size != 0){
                allSectors = response
                showPrevAndNext()
            }
        }
    }

    private inner class SectorSeatsSubscriber: BaseSubscriber<MutableList<SeatModel>> {

        var areaId: Int

        constructor(areaId: Int){
            this.areaId = areaId
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<SeatModel>) {
            super.onNext(response)
            if(response.isNotEmpty()){
                seatsCoordinates.value = response
                response.forEachIndexed { index, seatModel ->
                    if(seatModel.zone_id != null){
                        Log.d("fdfdgd", "not null")
                    }
                }
            }
            eventUseCase!!.getEventSectorStatuses(eventId, sectorId = sectorId!!.toInt(), areaId = areaId, SectorStatusesSubscriber())
        }
    }

    private inner class SectorStatusesSubscriber: BaseSubscriber<MutableList<StatusModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<StatusModel>) {
            super.onNext(response)

            response.forEach {
                if(it.state == FREE_PLACE){
                    orderUseCase!!.addToCart(eventId, areaId, it.sid!!, CreateOrderSubscriber())
                    return
                }
            }
        }
    }

    private inner class CreateOrderSubscriber: BaseSubscriber<OrderIdModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: OrderIdModel) {
            super.onNext(response)
            currentOrderId = response.order_id
            orderUseCase!!.getOrder(response.order_id, OrderSubscriber())
        }
    }

    private inner class AllOrdersSubscriber: BaseSubscriber<MutableList<OrderModel>> {

        var orderId: Int

        constructor(order: Int){
            this.orderId = order
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<OrderModel>) {
            super.onNext(response)
        }
    }

    private inner class OrderSubscriber: BaseSubscriber<OrderModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: OrderModel) {
            super.onNext(response)
            cart.value = response
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

    private fun payOrder(sid: String){
        orderUseCase!!.payOrder(sid.toInt(), PayOrderSubscriber())
    }

    private inner class PayOrderSubscriber: BaseSubscriber<ResponseBody>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: ResponseBody) {
            super.onNext(response)

        }
    }

    fun zoomIn(){
        zoomView.value = true
    }

    fun zoomOut(){
        zoomView.value = false
    }

    override fun onClick(view: View?) {
        when {
            view!!.id == R.id.mb_prev -> {
                sectorId = sectorPrevLive.value
                showPrevAndNext()
            }
            view.id == R.id.mb_next -> {
                sectorId = sectorNextLive.value
                showPrevAndNext()
            }
            view.id == R.id.mb_cart -> {
                var fragment = BasketFragment()
                fragment.arguments = bundleOf(
                    ORDER_ID to currentOrderId,
                )
                nextFragment.value = fragment
            }
        }
    }

    fun showPrevAndNext(){
        sectorLive.value = String.format(context.getString(R.string.sector), sectorId)
        allSectors.forEachIndexed { index, sectorModel ->
            if(sectorModel.view_id.toString() == sectorId){
                if(index != 0) {
                    sectorPrevLive.value = allSectors[index - 1].view_id.toString()
                }else{
                    sectorPrevLive.value = null
                }
                if (index != allSectors.size - 1)
                    sectorNextLive.value = allSectors[index + 1].view_id.toString()
                else{
                    sectorNextLive.value = null
                }
                return@forEachIndexed
            }
        }
    }

    private inner class SectorZonesSubscriber: BaseSubscriber<MutableList<ZoneModel>> {

        var areaId: Int

        constructor(areaId: Int){
            this.areaId = areaId
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<ZoneModel>) {
            super.onNext(response)
            pricesList.value = response
        }
    }

    override fun removeItem(position: Int) {
        var sid = list.value!![position].sid
        if(sid != null){
            orderUseCase!!.deleteOrder(eventId, areaId, sid, DeleteOrderSubscriber(position))
        }
    }

    private inner class DeleteOrderSubscriber: BaseSubscriber<ResponseBody> {

        var pos: Int

        constructor(pos: Int){
            this.pos = pos
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: ResponseBody) {
            super.onNext(response)
            previousDeletedItem = list.value?.get(pos)
            list.value?.removeAt(pos)
            _deletedPosition.value = pos
        }
    }

    override fun click(item: ZoneModel?) {

    }

    fun getListener(): ItemClickListener{
        return this
    }
}