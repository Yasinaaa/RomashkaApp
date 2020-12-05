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
import ru.android.romashkaapp.StartActivity.Companion.REPOSITORY
import ru.android.romashkaapp.afisha.adapters.MatchesAdapter
import ru.android.romashkaapp.base.BaseViewModel
import ru.android.romashkaapp.stadium.ItemClickListener
import ru.android.romashkaapp.utils.SvgFromXmlCreater.Companion.saveHtmlToLocal
import ru.android.romashkaapp.utils.Utils
import ru.android.romashkaapp.utils.parseTimeStamp

/**
 * Created by yasina on 15.10.2020.
 * Copyright (c) 2020 Infomatica. All rights reserved.
 */

public interface ItemClickListener{
    fun click(item: ZoneModel?)
}

class StadiumViewModel(application: Application) : BaseViewModel(application), View.OnClickListener,
    ItemClickListener {

    private var eventUseCase: EventsUseCase? = null
    private var dictionaryUseCase: DictionaryUseCase? = null
    private var orderUseCase: OrderUseCase? = null
    val pricesList: MutableLiveData<MutableList<ZoneModel>> = MutableLiveData()
    val zoomView = MutableLiveData<Boolean>()
    val title = MutableLiveData<String>()
    val time = MutableLiveData<String>()
    val championship = MutableLiveData<String>()
    val location = MutableLiveData<String>()
    val areaId = MutableLiveData<Int>()
    val toolbarView = MutableLiveData<Boolean>()
    val svgArea = MutableLiveData<String>()
    val selectedItem = MutableLiveData<Boolean>()
    private var eventId: Int? = null

    init {
        eventUseCase = EventsUseCase(REPOSITORY, Utils.getAccessToken(application)!!)
        orderUseCase = OrderUseCase(REPOSITORY, Utils.getAccessToken(application)!!)
        dictionaryUseCase = DictionaryUseCase(REPOSITORY, Utils.getAccessToken(application)!!)
    }

    fun getEvent(id: Int, championship: String?){
        eventId = id
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

                if(response.size >= 1) {
                    areaId.value = response[0].id
                    eventUseCase!!.getEventAreaPlan(
                        eventId!!,
                        response[0].id,
                        AreaPlanSubscriber(response[0].id)
                    )
                }
            }
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
            svgArea.value = saveHtmlToLocal(context, areaId, response.string())
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

    override fun click(item: ZoneModel?) {
//        nextFragmentOpenClick.value = item
    }

    fun getListener(): ItemClickListener{
        return this
    }

    fun onPricesClick(){
        eventUseCase!!.getEventSectorZones(eventId!!, areaId.value!!,  SectorZonesSubscriber(areaId.value!!))
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

    fun choosePrices(){
        selectedItem.value = true
    }

    fun resetPrices(){
        selectedItem.value = false
    }
}