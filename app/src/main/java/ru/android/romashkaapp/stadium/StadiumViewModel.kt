package ru.android.romashkaapp.stadium

import android.app.Application
import android.ru.romashkaapp.models.*
import android.ru.romashkaapp.usecases.DictionaryUseCase
import android.ru.romashkaapp.usecases.EventsUseCase
import android.ru.romashkaapp.usecases.OrderUseCase
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import okhttp3.ResponseBody
import ru.android.romashkaapp.BaseSubscriber
import ru.android.romashkaapp.StartActivity
import ru.android.romashkaapp.StartActivity.Companion.REPOSITORY
import ru.android.romashkaapp.afisha.adapters.MatchesAdapter
import ru.android.romashkaapp.base.BaseViewModel
import ru.android.romashkaapp.matches.ItemClickListener
import ru.android.romashkaapp.utils.Utils
import ru.android.romashkaapp.utils.parseTimeStamp

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */
public interface ItemClickListener{
    fun click(item: EventModel?)
}

class StadiumViewModel(application: Application) : BaseViewModel(application), View.OnClickListener,
    ItemClickListener {

    private var eventUseCase: EventsUseCase? = null
    private var dictionaryUseCase: DictionaryUseCase? = null
    private var orderUseCase: OrderUseCase? = null
    private val pricesList: MutableLiveData<MutableList<EventModel?>> = MutableLiveData()
    val zoomView = MutableLiveData<Boolean>()
    val title = MutableLiveData<String>()
    val time = MutableLiveData<String>()
    val championship = MutableLiveData<String>()
    val location = MutableLiveData<String>()
    val toolbarView = MutableLiveData<Boolean>()
    val priceClick = MutableLiveData<Boolean>()
    val svgArea = MutableLiveData<String>()
    private var eventId: Int? = null

    init {
        eventUseCase = EventsUseCase(REPOSITORY, Utils.getAccessToken(application)!!)
        orderUseCase = OrderUseCase(REPOSITORY, Utils.getAccessToken(application)!!)
        dictionaryUseCase = DictionaryUseCase(REPOSITORY, Utils.getAccessToken(application)!!)
        pricesList.value = arrayListOf(EventModel(), EventModel(), EventModel(), EventModel(), EventModel())
    }

    fun getEvent(id: Int, championship: String?){
        eventId = id

        eventId = 180 //todo remove

        eventUseCase!!.getEvent(eventId!!, EventSubscriber(championship))
    }

    private inner class EventSubscriber: BaseSubscriber<EventModel> {

        var championshipTitle: String? = null

        constructor(championship: String?){
            this.championshipTitle = championship
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: EventModel) {
            super.onNext(response)
            Log.d("ffd", "EventSubscriber")
            title.value = response.name
            time.value = response.sdate.parseTimeStamp()
            championship.value = championshipTitle
//            dictionaryUseCase!!.getHall(response.hall_id, HallSubscriber())
            eventUseCase!!.getEventAreas(response.id, AreasSubscriber())
//            eventUseCase!!.getEventAreas(response.id, AreasSubscriber())
        }
    }

    private inner class AreasSubscriber: BaseSubscriber<MutableList<AreaModel>>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: MutableList<AreaModel>) {
            super.onNext(response)
            if(response.size == 1){
                location.value = response[0].name
                eventUseCase!!.getEventArea(eventId!!, response[0].id, AreaSubscriber())
            }
        }
    }

    private inner class AreaSubscriber: BaseSubscriber<AreaModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: AreaModel) {
            super.onNext(response)

//            svgArea.value = "http://192.168.2.80/yasina/v1/events/" + eventId!! + "/areas/" + response.id + "/plan?type=svg&accessToken=" +
//                    Utils.getAccessToken(context)!!
            eventUseCase!!.getEventAreaPlan(eventId!!, response.id, AreaPlanSubscriber(response.id))




            //eventUseCase!!.getEventSectorSeats(eventId!!, 1,  response.id, "coordinates", SectorSeatsSubscriber(response.id))
        }
    }

    private inner class AreaPlanSubscriber: BaseSubscriber<ResponseBody> {

        var areaId: Int

        constructor(areaId: Int){
            this.areaId = areaId
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: ResponseBody) {
            super.onNext(response)
            var s = response.string()
            Log.d("dss", s)
            //eventUseCase!!.getEventSectorSeats(eventId!!, 1,  areaId, "coordinates", SectorSeatsSubscriber(areaId))
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
            eventUseCase!!.getEventSectorZones(eventId!!, areaId,  SectorZonesSubscriber(areaId))
//            eventUseCase!!.getEventSectorStatuses(eventId!!, 1, areaId, SectorStatusesSubscriber())

            orderUseCase!!.createOrders(eventId!!, areaId, response[0].sid!!, CreateOrderSubscriber())
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
            eventUseCase!!.getEventSectorStatuses(eventId!!, 1, areaId, SectorStatusesSubscriber())

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

    private inner class HallSubscriber: BaseSubscriber<HallModel>() {

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onNext(response: HallModel) {
            super.onNext(response)
            Log.d("ffd", "HallSubscriber")
            location.value = response.name
        }
    }

    fun zoomIn(){
        zoomView.value = true
    }

    fun zoomOut(){
        zoomView.value = false
    }

    fun toolbarView(){
        toolbarView.value = toolbarView.value != true
    }

    override fun onClick(view: View?) {
        TODO("Not yet implemented")
    }

    override fun click(item: MatchesAdapter.Match?) {
//        nextFragmentOpenClick.value = item
    }

    fun getListener(): ItemClickListener{
        return this
    }

    fun onPricesClick(){
        priceClick.value = true
    }
}