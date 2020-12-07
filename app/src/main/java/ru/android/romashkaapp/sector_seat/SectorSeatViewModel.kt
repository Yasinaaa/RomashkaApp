package ru.android.romashkaapp.sector_seat

import android.app.Application
import android.ru.romashkaapp.models.*
import android.ru.romashkaapp.usecases.DictionaryUseCase
import android.ru.romashkaapp.usecases.EventsUseCase
import android.ru.romashkaapp.usecases.OrderUseCase
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.ResponseBody
import ru.android.romashkaapp.BaseSubscriber
import ru.android.romashkaapp.R
import ru.android.romashkaapp.StartActivity
import ru.android.romashkaapp.adapter.helpers.SwipeRemoveActionListener
import ru.android.romashkaapp.base.BaseViewModel
import ru.android.romashkaapp.stadium.ItemClickListener
import ru.android.romashkaapp.stadium.adapters.FullPricesAdapter
import ru.android.romashkaapp.utils.Utils

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
class SectorSeatViewModel(application: Application) : BaseViewModel(application), View.OnClickListener, SwipeRemoveActionListener,
    ItemClickListener {

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
    val sectorLive = MutableLiveData<String>()
    val sectorPrevLive = MutableLiveData<String?>()
    val sectorNextLive = MutableLiveData<String?>()
    var allSectors: MutableList<SectorModel> = mutableListOf()
    val pricesList: MutableLiveData<MutableList<ZoneModel>> = MutableLiveData()

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
        sectorLive.value = String.format(context.getString(R.string.sector), sectorId)

//        eventUseCase!!.getEventSectorSeats(eventId=eventId, areaId = areaId, sectorId = sectorId.toString(), type = null,
//                        useCaseDisposable = SectorSeatsSubscriber(areaId))

        eventUseCase!!.getEventSectorZones(eventId, areaId,  SectorZonesSubscriber(areaId))
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
                response.forEachIndexed { index, seatModel ->
                    if(seatModel.zone_id != null){
                        Log.d("fdfdgd", "not null")
                    }
                }
            }
//            svgArea.value = saveHtmlToLocal(context, areaId, response.string())
//
//            eventUseCase!!.getEventSectorStatuses(eventId!!, 1, areaId, SectorStatusesSubscriber())
//            orderUseCase!!.createOrders(eventId, areaId, response[5].sid!!, CreateOrderSubscriber())
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
        if (view!!.id == R.id.mb_prev){
            sectorId = sectorPrevLive.value
            showPrevAndNext()
        }else if (view.id == R.id.mb_next){
            sectorId = sectorNextLive.value
            showPrevAndNext()
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
        previousDeletedItem = _list.value?.get(position)
        _list.value?.removeAt(position)
        _deletedPosition.value = position
    }

    override fun click(item: ZoneModel?) {

    }

    fun getListener(): ItemClickListener{
        return this
    }
}