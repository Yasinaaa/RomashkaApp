package ru.android.romashkaapp.sector_seat

import android.app.Application
import android.ru.romashkaapp.models.CartModel
import android.ru.romashkaapp.models.SeatModel
import android.ru.romashkaapp.models.StatusModel
import android.ru.romashkaapp.usecases.DictionaryUseCase
import android.ru.romashkaapp.usecases.EventsUseCase
import android.ru.romashkaapp.usecases.OrderUseCase
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.ResponseBody
import ru.android.romashkaapp.BaseSubscriber
import ru.android.romashkaapp.StartActivity
import ru.android.romashkaapp.adapter.helpers.SwipeRemoveActionListener
import ru.android.romashkaapp.base.BaseViewModel
import ru.android.romashkaapp.utils.SvgFromXmlCreater.Companion.saveHtmlToLocal
import ru.android.romashkaapp.utils.Utils

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class SectorSeatViewModel(application: Application) : BaseViewModel(application), View.OnClickListener, SwipeRemoveActionListener {

    val zoomView = MutableLiveData<Boolean>()

    private val _list: MutableLiveData<MutableList<CartModel>> = MutableLiveData()
    val list: LiveData<MutableList<CartModel>> = _list

    private val _deletedPosition: MutableLiveData<Int> = MutableLiveData()
    val removedItemIndex: LiveData<Int> = _deletedPosition
    private var previousDeletedItem: CartModel? = null

    private var eventUseCase: EventsUseCase? = null
    private var dictionaryUseCase: DictionaryUseCase? = null
    private var orderUseCase: OrderUseCase? = null
    val svgArea = MutableLiveData<String>()
    private var eventId: Int = 0
    private var areaId: Int = 0
    private var sectorId: String? = null

    init {
        eventUseCase = EventsUseCase(StartActivity.REPOSITORY, Utils.getAccessToken(application)!!)
        orderUseCase = OrderUseCase(StartActivity.REPOSITORY, Utils.getAccessToken(application)!!)
        dictionaryUseCase = DictionaryUseCase(StartActivity.REPOSITORY, Utils.getAccessToken(application)!!)


        _list.value = arrayListOf(CartModel(), CartModel(), CartModel())
    }

    fun getSeats(eventId: Int, areaId: Int, sectorId: String?){
        this.eventId = eventId
        this.areaId = areaId
        this.sectorId = sectorId
        eventUseCase!!.getEventSectorSeats(eventId=eventId, areaId = areaId, sectorId = sectorId, type = null,
            useCaseDisposable = SectorSeatsSubscriber(areaId))
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
//            svgArea.value = saveHtmlToLocal(context, areaId, response.string())
//            eventUseCase!!.getEventSectorZones(eventId!!, areaId,  SectorZonesSubscriber(areaId))
//            eventUseCase!!.getEventSectorStatuses(eventId!!, 1, areaId, SectorStatusesSubscriber())
//            orderUseCase!!.createOrders(eventId, areaId, response[0].sid!!, CreateOrderSubscriber())
        }
    }

    private inner class SectorStatusesSubscriber: BaseSubscriber<MutableList<StatusModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<StatusModel>) {
            super.onNext(response)
        }
    }

    private inner class CreateOrderSubscriber: BaseSubscriber<ResponseBody>() {

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
        TODO("Not yet implemented")
    }

    override fun removeItem(position: Int) {
        previousDeletedItem = _list.value?.get(position)
        _list.value?.removeAt(position)
        _deletedPosition.value = position
    }

}